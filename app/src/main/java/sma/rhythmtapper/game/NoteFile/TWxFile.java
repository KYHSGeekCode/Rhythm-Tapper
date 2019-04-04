package sma.rhythmtapper.game.NoteFile;
import java.io.*;
import java.util.*;
import sma.rhythmtapper.game.models.*;
import android.util.*;
import org.json.*;
import android.graphics.*;

public class TWxFile
{
	public static List<Ball> Read(File file) throws FileNotFoundException, JSONException
	{
		ArrayList<Ball> balls = new ArrayList<>();
		FileInputStream finputstream = new FileInputStream(file);

		//JsonReader jreader = new JsonReader(freader);
		JSONTokener jtokener = new JSONTokener(readStream(finputstream));
		JSONObject jobject = new JSONObject(jtokener);
		int version = jobject.getInt("version");
		JSONObject metadata = jobject.getJSONObject("metadata");
		JSONArray notes = jobject.getJSONArray("notes");
		int numnotes = notes.length();
		for(int i=0;i<numnotes;i++)
		{
			JSONObject note = notes.getJSONObject(i);
			int id = note.getInt("ID");
			JSONArray color = note.getJSONArray("Color");
			int mode = note.getInt("Mode");
			int flick = note.getInt("Flick");
			float time = (float) note.getDouble("Time");
			float startLine = (float) note.getDouble("StartLine");
			float endLine = (float) note.getDouble("EndLine");
			JSONArray prevIDs = note.getJSONArray("PrevIDs");
			int col = Color.argb(color.getInt(3),color.getInt(0),color.getInt(1),color.getInt(2));
			balls.add(new Ball(id,col,mode,flick,time,startLine,endLine,toIntArray(prevIDs)));
		}
		return balls;
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

	public static int[] toIntArray(JSONArray jarr) throws JSONException
	{
		int[] ret = new int[jarr.length()];
		for(int i=0;i<ret.length;i++)
		{
			ret[i] = jarr.getInt(i);
		}
		return ret;
	}
}
