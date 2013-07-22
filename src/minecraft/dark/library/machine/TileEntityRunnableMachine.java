package dark.library.machine;

import java.util.Random;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.oredict.OreDictionary;
import universalelectricity.compatibility.TileEntityUniversalElectrical;
import universalelectricity.core.electricity.ElectricityPack;
import dark.core.api.IDisableable;
import dark.core.api.PowerSystems;

public class TileEntityRunnableMachine extends TileEntityUniversalElectrical implements IDisableable
{

	/** Forge Ore Directory name of the item to toggle power */
	public static String powerToggleItemID = "battery";

	/** Power Systems this machine can support */
	protected PowerSystems[] powerList = new PowerSystems[] { PowerSystems.BUILDCRAFT, PowerSystems.MEKANISM, PowerSystems.INDUSTRIALCRAFT };

	protected Random random = new Random();
	protected int ticksDisabled = 0;
	protected boolean runWithOutPower = false;

	@Override
	public void updateEntity()
	{
		super.updateEntity();

		if (this.ticksDisabled > 0)
		{
			this.ticksDisabled--;
			this.whileDisable();
		}
	}

	/** Does this tile have power to run and do work */
	public boolean canRun()
	{
		;
		return !this.isDisabled() && (this.runWithOutPower || PowerSystems.runPowerLess(powerList));
	}

	/** Called when a player activates the tile's block */
	public boolean onPlayerActivated(EntityPlayer player)
	{
		if (player != null && player.capabilities.isCreativeMode)
		{
			ItemStack itemStack = player.getHeldItem();
			if (itemStack != null)
			{
				for (ItemStack stack : OreDictionary.getOres(TileEntityRunnableMachine.powerToggleItemID))
				{
					if (stack.isItemEqual(itemStack))
					{
						this.runWithOutPower = !this.runWithOutPower;
						//player.sendChatToPlayer(chatmessagecomponent)
						return true;
					}
				}
			}
		}
		return false;
	}

	@Override
	public float receiveElectricity(ForgeDirection from, ElectricityPack receive, boolean doReceive)
	{
		if (!this.runWithOutPower && receive != null && this.canConnect(from))
		{
			// Only do voltage disable if the voltage is higher than the peek voltage and if random chance
			//TODO replace random with timed damage to only disable after so many ticks
			if (receive != null && receive.voltage > (Math.sqrt(2) * this.getVoltage()) && this.random.nextBoolean())
			{
				if (doReceive)
				{
					this.onDisable(20 + this.random.nextInt(100));
				}
				return 0;
			}
			return super.receiveElectricity(from, receive, doReceive);

		}
		return 0;
	}

	@Override
	public float getRequest(ForgeDirection direction)
	{
		return Math.max(this.getMaxEnergyStored() - this.getEnergyStored(), 0);
	}

	@Override
	public float getProvide(ForgeDirection direction)
	{
		return 0;
	}

	@Override
	public float getMaxEnergyStored()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	/** Called every tick while this tile entity is disabled. */
	protected void whileDisable()
	{
		//TODO generate electric sparks
	}

	@Override
	public void onDisable(int duration)
	{
		this.ticksDisabled = duration;
	}

	@Override
	public boolean isDisabled()
	{
		return this.ticksDisabled > 0;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		this.ticksDisabled = nbt.getInteger("disabledTicks");
		this.runWithOutPower = nbt.getBoolean("shouldPower");
		if (nbt.hasKey("wattsReceived"))
		{
			this.energyStored = (float) nbt.getDouble("wattsReceived");
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setInteger("disabledTicks", this.ticksDisabled);
		nbt.setBoolean("shouldPower", this.runWithOutPower);
	}
}
