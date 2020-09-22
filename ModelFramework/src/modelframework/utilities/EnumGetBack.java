/**
 * 
 */
package modelframework.utilities;

import java.util.EnumSet;
import java.util.Iterator;

import modelframework.interfaces.IEnumable;

/**
 * Get the Enum Object back from It's Int Value and the Enum Class
 *
 */
public class EnumGetBack
	{
		/*******************************************************************
		 * convert int Value to Enum
		 * 
		 * @param clzz
		 *           - Enum Class
		 *           may not be null
		 * @param intValue
		 * @return e with Matching Value
		 * 
		 *******************************************************************/
		@SuppressWarnings("unchecked")
		public static <E extends Enum<E>> E lookupEnum(Class<E> clzz, int intValue)
			{
				EnumSet<E> set = EnumSet.allOf(clzz);
				E rval = null;
				Iterator<E> iter = set.iterator();
				for (int i = 0; i < set.size(); i++)
					{
						rval = iter.next();
						if (rval instanceof IEnumable)
							{
								IEnumable cusenum = (IEnumable) rval;
								if (cusenum.Get_Value_From_Enums(cusenum) == intValue)
									{
										return (E) cusenum.valueOf(intValue);
									}
							}
							
					}
					
				return rval;
				
			}
	}
