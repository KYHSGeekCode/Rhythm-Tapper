package sma.rhythmtapper.game.NoteFile;
import java.io.*;
import java.util.*;
import sma.rhythmtapper.game.models.*;
import android.util.*;
import org.json.*;

public class TWxFile
{
	public List<Ball> Read(File file) throws FileNotFoundException, JSONException
	{
		ArrayList<Ball> balls = new ArrayList<>();
		FileInputStream finputstream = new FileInputStream(file);
		
		//JsonReader jreader = new JsonReader(freader);
		JSONTokener jtokener = new JSONTokener(readStream(finputstream));
		JSONObject jobject = new JSONObject(jtokener);
		
	}
	
	public static String readStream(InputStream is) {
		StringBuilder sb = new StringBuilder(512);
		try {
			Reader r = new InputStreamReader(is, "UTF-8");
			int c = 0;
			while ((c = r.read()) != -1) {
				sb.append((char) c);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return sb.toString();
	}
	//https://stackoverflow.com/a/3849771/8614565
}
