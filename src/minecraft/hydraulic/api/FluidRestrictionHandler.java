package hydraulic.api;

import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.liquids.LiquidDictionary;
import net.minecraftforge.liquids.LiquidDictionary.LiquidRegisterEvent;
import net.minecraftforge.liquids.LiquidStack;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public class FluidRestrictionHandler
{
	private static BiMap<ColorCode, LiquidStack> restrictedStacks = HashBiMap.create();

	static
	{
		/* ADD DEFAULT LIQUIDS */
		restrictedStacks.put(ColorCode.BLUE, LiquidDictionary.getCanonicalLiquid("Water"));
		restrictedStacks.put(ColorCode.RED, LiquidDictionary.getCanonicalLiquid("Lava"));
	}

	@ForgeSubscribe
	public void onLiquidRegistered(LiquidRegisterEvent event)
	{
		if (event.Name != null)
		{
			if (event.Name.equalsIgnoreCase("Fuel") && !restrictedStacks.containsKey(ColorCode.YELLOW))
			{
				restrictedStacks.put(ColorCode.YELLOW, event.Liquid);
			}
			else if (event.Name.equalsIgnoreCase("Oil") && !restrictedStacks.containsKey(ColorCode.BLACK))
			{
				restrictedStacks.put(ColorCode.BLACK, event.Liquid);
			}
			else if (event.Name.equalsIgnoreCase("Milk") && !restrictedStacks.containsKey(ColorCode.WHITE))
			{
				restrictedStacks.put(ColorCode.WHITE, event.Liquid);
			}
		}
	}

	/**
	 * Checks too see if a color has a restricted stack
	 */
	public static boolean hasRestrictedStack(int meta)
	{
		return restrictedStacks.containsKey(ColorCode.get(meta));
	}
	public static boolean hasRestrictedStack(LiquidStack stack)
	{
		if(stack == null)
		{
			return false;
		}					
		return restrictedStacks.inverse().containsKey(stack);
	}
	/**
	 * gets the liquid stack that is restricted to this color
	 * 
	 */
	public static LiquidStack getStackForColor(ColorCode color)
	{
		return restrictedStacks.get(color);
	}
}