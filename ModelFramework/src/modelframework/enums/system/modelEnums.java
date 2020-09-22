package modelframework.enums.system;

public class modelEnums
	{
		
		//Enumeration for Object Type Classification
		
		public enum objectType
			{
			ROOT_OBJECT(0), DEPENDANT_OBJECT(1);
				
				private final int value;
				
				objectType(int value)
					{
						this.value = value;
					}
					
				public int GetobjectType()
					{
						return value;
					}
			}
			
		//Enumeration for Entity Mode Classification
		
		public enum entityMode
			{
			CREATE(1), //Creation Mode
			LOCKED(4),//Entity is Locked for Changes: Lock is mandatory for Changing or Deleting the Entity 
			CHANGE(2),//Entity is in Change Mode
			DELETE(3), //PBO: Entity to be Deleted in Db and then changed to Deleted in Buffer(Entity Manager)
			REFRESHED(5),//Entity Refreshed after Dbase Commit or Just Selected using Query Object
			VIEW(0); //For Reports Only: Entity Cannot be changed in this Mode
				
				private final int value;
				
				entityMode(int value)
					{
						this.value = value;
					}
					
				public int GetentityMode()
					{
						return value;
					}
			}
			
	}
