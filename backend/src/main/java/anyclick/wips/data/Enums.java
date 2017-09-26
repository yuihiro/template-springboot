package anyclick.wips.data;

public class Enums {

	public enum MapperType {
		TINY("TINY"), LIST("LIST"), INFO("INFO");

		private final String value;

		MapperType(String value) {
			this.value = value;
		}

		@Override
		public String toString() {
			return value + "";
		}
	}

	public enum UserStatus {
		AA("AA"), BB("BB"), NULL("CC");

		private final String value;

		UserStatus(String value) {
			this.value = value;
		}

		@Override
		public String toString() {
			return value + "";
		}

	}

	public enum City {
		Seoul, Busan, Daegu, Gwangju, Daejun
	}

	public enum SensorType {

		CONTINUE(100), STOP(200), PAUSE(300);

		private final int value;

		SensorType(int value) {
			this.value = value;
		}

		@Override
		public String toString() {
			return value + "";
		}
	}

	public enum ApType {

		CONTINUE(100), STOP(200), PAUSE(300);

		private final int value;

		ApType(int value) {
			this.value = value;
		}

		@Override
		public String toString() {
			return value + "";
		}
	}
}