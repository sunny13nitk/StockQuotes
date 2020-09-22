package mypackage.enums;

import java.util.HashMap;
import java.util.Map;

import modelframework.interfaces.IEnumable;

public class Enums
	{
		
		// Book Type Enum
		@SuppressWarnings("unchecked")
		public enum Book_Type implements IEnumable
			{
			Income(1), Expense(2), Savings(3), Home_Loan(4), Liability(5), Reimbursement(6), Home_Kalka(7), Trading(8), Profit(9), Loss(10), Income_Tax(
			      11), None(0);
				
				private final int		value;
				@SuppressWarnings("rawtypes")
				private static Map	map	= new HashMap<>();
				
				private Book_Type(int value)
					{
						this.value = value;
					}
					
				public int getValue()
					{
						return value;
					}
					
				static
					{
						for (Book_Type BookType : Book_Type.values())
							{
								map.put(BookType.value, BookType);
							}
					}
					
				public Object valueOf(int intValue)
					{
						return (Book_Type) map.get(intValue);
					}
					
				@Override
				public int Get_Value_From_Enums(Object Enum)
					{
						int value = 0;
						if (Enum != null)
							{
								Book_Type bk_type = (Book_Type) Enum;
								value = bk_type.getValue();
							}
						return value;
					}
					
			}
			
		// Account Type Enum
		@SuppressWarnings("unchecked")
		public enum Account_Type implements IEnumable
			{
			Savings(1), Salary(6), Third_Party(7), Current(8), RD(2), FD(3), Demat(4), CCard(5), PPF(9), EPF(10), LIC_Policy(11), Sundry(
			      12), Profit_Loss(13), Income_Tax(14);
				
				private final int		value;
				@SuppressWarnings("rawtypes")
				private static Map	map	= new HashMap<>();
				static
					{
						for (Account_Type ACType : Account_Type.values())
							{
								map.put(ACType.value, ACType);
							}
					}
					
				private Account_Type(int value)
					{
						this.value = value;
					}
					
				public int getValue()
					{
						return value;
					}
					
				@Override
				public int Get_Value_From_Enums(Object Enum)
					{
						int value = 0;
						if (Enum != null)
							{
								Account_Type acc_type = (Account_Type) Enum;
								value = acc_type.getValue();
							}
						return value;
					}
					
				public Object valueOf(int intValue)
					{
						return (Account_Type) map.get(intValue);
					}
			}
			
	}
