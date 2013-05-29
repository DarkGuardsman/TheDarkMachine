package hydraulic.fluidnetwork;

import hydraulic.api.IColorCoded;
import hydraulic.api.IPipeConnection;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.liquids.ILiquidTank;
import net.minecraftforge.liquids.ITankContainer;
import net.minecraftforge.liquids.LiquidStack;

/**
 * A machine that acts as one with the liquid network using the networks pressure for some function
 * that doesn't change the over all network pressure. So pipes, gauges, tubes, buffers, decor
 * blocks.
 */
public interface IFluidNetworkPart extends IPipeConnection, IColorCoded, ITankContainer,  INetworkPath
{
	/**
	 * gets the devices pressure from a given side for input
	 */
	public double getMaxPressure(ForgeDirection side);

	/**
	 * The max amount of liquid that can flow per request
	 */
	public int getMaxFlowRate(LiquidStack stack, ForgeDirection side);	

	/**
	 * Called when the pressure on the machine reachs max
	 * 
	 * @param damageAllowed - can this tileEntity cause grief damage
	 * @return true if the device over pressured and destroyed itself
	 */
	public boolean onOverPressure(Boolean damageAllowed);

	/**
	 * size of the pipes liquid storage ability
	 */
	public int getTankSize();
	
	public ILiquidTank getTank();
	
	public void setTankContent(LiquidStack stack);
	

}