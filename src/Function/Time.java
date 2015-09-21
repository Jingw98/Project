package Function;

public class Time {
	public static String secondToRuntime(int time) {
		StringBuffer buffer = new StringBuffer();
		if (time >= 3600) {
			if (time / 3600 > 10) {
				buffer.append(Integer.toString(time / 3600) + ":");
			} else {
				buffer.append("0" + Integer.toString(time / 3600) + ":");
			}
			time = time - 3600 * (time / 3600);
		} else {
			buffer.append("00:");
		}

		if (time >= 60) {
			if (time / 60 > 10) {
				buffer.append(Integer.toString(time / 60) + ":");
			} else {
				buffer.append("0" + Integer.toString(time / 60) + ":");
			}
			time = time - 60 * (time / 60);
		} else {
			buffer.append("00:");
		}

		if (time >= 10) {
			buffer.append(Integer.toString(time));
		} else {
			buffer.append("0" + Integer.toString(time));
		}
		return buffer.toString();
	}

	public static int runtimeToSecond(String str) {
		String[] hms = str.trim().split(":");
		if (hms.length != 3) {
			return 0;
		}
		int time = Integer.valueOf(hms[0]) * 60 * 60 + Integer.valueOf(hms[1])
				* 60 + Integer.valueOf(hms[2]);
		return time;
	}

	public static String setTotalTime(int time) {
		int t = time / 1000;
		int hours = t / 3600;
		int minutes = t / 60 - 60 * hours;
		int seconds = (t % 3600) % 60;
		String m = Integer.toString(minutes), s = Integer.toString(seconds);
		if (minutes < 10) {
			m = "0" + Integer.toString(minutes);
		}
		if (seconds < 10) {
			s = "0" + Integer.toString(seconds);
		}

		return "0" + hours + ":" + m + ":" + s;
		// move.setMaximum((
		// mediaPlayerComponent).getMediaPlayer().getLength());

	}

}
