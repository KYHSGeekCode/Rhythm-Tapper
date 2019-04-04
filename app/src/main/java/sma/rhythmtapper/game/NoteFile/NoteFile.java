package sma.rhythmtapper.game.NoteFile;

import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;

import java.io.*;
import java.util.*;

import sma.rhythmtapper.game.models.*;

public class NoteFile implements Serializable
{
    final String TAG = "NoteFile";

    //dir should end with /
    public NoteFile(File dir)
	{
        this.dir = dir;
        isLoaded = false;
        //file is the root directory of the project
        //file/*.mp3,wav
        //file/easy.notemap2
        //file/normal.notemap2
        //file/hard.notemap2
        //file/master.notemap2
        //file/master+.notemap2
        File[] files = dir.listFiles();
		if (files == null)
		{
			Log.v(TAG, "Error " + dir.getPath());
			return;
		}
        for (File file : files)
		{
            String filename = file.getName();
            String extension = filename.substring(filename.lastIndexOf("."));
            if (extension.compareToIgnoreCase(".mp3") == 0 || extension.compareToIgnoreCase(".wav") == 0)
			{
                musicFile = file;
                Log.v("a", "musicfile=" + musicFile);
                break;
            }
        }
        File infoFile = new File(dir, "info.txt");
		if(!infoFile.exists())
		{
		    songName = dir.getName();
			//simple add
			//parse possible tw5 files
            //Check for files end with .tw5
            //get affix split by _
            for(File file : files)
            {
                String filename = file.getName();
                int litw5 = filename.lastIndexOf(".tw5");
                if(litw5>0)
                {
                    int li_ = filename.lastIndexOf('_');
                    String affix = filename.substring(li_,litw5);
                    int idx = 0;
                    switch (affix.toLowerCase())
                    {
                        case "easy":
                        case "debut":
                            idx  = 0;
                            break;
                        case "regular":
                        case "normal":
                            idx = 1;
                            break;
                        case "hard":
                        case "pro":
                            idx = 2;
                            break;
                        case "master":
                            idx = 3;
                            break;
                        case "master+":
                        case "apex":
                            idx = 4;
                            break;
                    }
                    notemapFiles.get(idx).add(file);
                }
            }

            return;
            //balls = TWxFile.Read();
		}
        //parse info.txt
        //String songName;
        String artist = "?";
        int difficulties[] = new int[5];

        try
		{
            BufferedReader br = new BufferedReader(new FileReader(infoFile));
            String line = br.readLine();
            while (line != null)
			{
                Log.v(TAG, "line=" + line);
                //I hate BOM!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                /*
				 <a href="http://www.faqs.org/rfcs/rfc3629.html">RFC 3629 - UTF-8, a transformation format of ISO 10646</a>
				 *
				 * <p>The
                 * <a href="http://www.unicode.org/unicode/faq/utf_bom.html">Unicode FAQ</a>
				 * defines 5 types of BOMs:<ul>
				 * <li><pre>00 00 FE FF  = UTF-32, big-endian</pre></li>
				 * <li><pre>FF FE 00 00  = UTF-32, little-endian</pre></li>
                 * <li><pre>FE FF        = UTF-16, big-endian</pre></li>
				 * <li><pre>FF FE        = UTF-16, little-endian</pre></li>
                 * <li><pre>EF BB BF     = UTF-8</pre></li>
				 * </ul></p>
				 *
				 * https://stackoverflow.com/questions/1835430/byte-order-mark-screws-up-file-reading-in-java
                 */
                line = line.replace("\uEFBB\u00BF", "");
                line = line.replace("\u0000\uFEFF", "");
                line = line.replace("\uFFFE\u0000", "");
                line = line.replace("\uFEFF", "");
                line = line.replace("\uFFFE", "");
                Log.v(TAG, Arrays.toString(line.getBytes()));
                Log.v(TAG, Arrays.toString("#title".getBytes()));
                if (line.startsWith("#title"))
				{
                    Log.v(TAG, "startswith");
                    line = line.replace("#title ", "").trim();
                    songName = line;
                }
				else if (line.startsWith("#artist"))
				{
                    line = line.replace("#artist ", "").trim();
                    artist = line;
                }
				else if (line.startsWith("#easy"))
				{
					line = line.replaceAll("[^0-9]", "");
                    difficulties[0] = Integer.parseInt(line.replace("#easy ", "").trim());

                }
				else if (line.startsWith("#normal"))
				{
					line = line.replaceAll("[^0-9]", "");
                    difficulties[1] = Integer.parseInt(line.replace("#normal ", "").trim());

                }
				else if (line.startsWith("#hard"))
				{
					line = line.replaceAll("[^0-9]", "");
                    difficulties[2] = Integer.parseInt(line.replace("#hard ", "").trim());
                }
				else if (line.startsWith("#master"))
				{
					line = line.replaceAll("[^0-9]", "");
                    difficulties[3] = Integer.parseInt(line.replace("#master ", "").trim());
                }
				else if (line.startsWith("#apex"))
				{
					line = line.replaceAll("[^0-9]", "");
                    difficulties[4] = Integer.parseInt(line.replace("#apex ", "").trim());
                }
                line = br.readLine();
            }
        }
		catch (IOException | NumberFormatException e)
		{
            throw new RuntimeException(e);
        }
        //info.txt parse done.
        Log.v(TAG, "Info.txt parse done.");
        Log.v(TAG, "Song name=" + songName);
        Log.v(TAG, "Difficulties=" + Arrays.toString(difficulties));
        Log.v(TAG, "Artist=" + artist);
        Log.v(TAG, "Folder=" + dir.getName());
    }
	/*
     if (files[i].Extension.Equals(".tw1")) { ReadTWxMetadata(files[i].FullName, 4, ref hasError); }
	 else if (files[i].Extension.Equals(".tw2")) { ReadTWxMetadata(files[i].FullName, 1, ref hasError); }
	 else if (files[i].Extension.Equals(".tw4")) { ReadTWxMetadata(files[i].FullName, 2, ref hasError); }
	 else if (files[i].Extension.Equals(".txt")) { ReadDelesteMetadata(files[i].FullName, ref hasError); }
	 else if (files[i].Extension.Equals(".json")) { ReadSSTrainMetadata(files[i].FullName, ref hasError); }
	 else if (files[i].Extension.Equals(".tw5")) { ReadTWxMetadata(files[i].FullName, 0, ref hasError); }
	 else if (files[i].Extension.Equals(".tw6")) { ReadTWxMetadata(files[i].FullName, 3, ref hasError); }
	 */
    public void Load(Difficulties difficulty)
    {

        try {
            balls= TWxFile.Read(notemapFiles.get(difficulty.ordinal()).get(0));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IndexOutOfBoundsException e) {
            Log.e(TAG,"",e);
        }
        /*
        //candidates:
        //SongName_Diff.tw2,4,txt,json,tw5,tw6,notemap2
        String baseName=songName + "_" + difficulty.getFileName() + ".";
        String[] candidates=new String[]{"txt","notemap2","json","tw5","tw2","tw4","tw6"};
        for (String candidate:candidates)
        {
            File candidateFile=new File(dir, baseName + candidate);
            if (candidateFile.exists())
            {
                switch (candidate)
                {
                    case "txt":
                        LoadDeleste(candidateFile);
                        return;
                    case "notemap2":
                        LoadNotemap2(candidateFile);
                        return;
                    case "json":
                        LoadSSTrain(candidateFile);
                        return;
                    case "tw5":
                    case "tw2":
                    case "tw4":
                    case "tw6":
                        LoadTWX(candidateFile);
                        return;
                    default:
                        break;
                }
            }
        }*/
    }

    private void LoadTWX(File candidateFile)
	{

    }

    private void LoadSSTrain(File candidateFile)
	{

    }

    private void LoadDeleste(File candidateFile)
	{
        //1. parse metadata
        try
		{
            BufferedReader br= new BufferedReader(new FileReader(candidateFile));
            String line=br.readLine();
            String title;
            String lyricist;
            String composer;
            String background;
            String song;
            String lyrics;
            double bpm;
            int offset;
            int movieoffset;
            Difficulties difficulty;
            int lv;
            int bgmvol;
            int sevol;
            ColorType attribute;
            while (line != null)
            {
                //process one line
                //if (line.startsWith(blah) line= line.replace(blah); data= line; else if ......
                char[] chs=line.toCharArray();
                if (chs.length < 3)
                {
                    //ignore that line
                    continue;
                }
                if (Character.isDigit(chs[1]))
                {
                    //maybe the note info, not metadata
                    line = line.replace("#", "");
                    String [] data=line.split(":");
                    /*
					 위 영상의 전주부분 채보코드
					 #0,000:20202220:54555:41441
					 #1,000:00200020:44:55
					 #0,001:20202220:54555:41441
					 #1,001:00200020:44:55
					 #0,002:2000200011133333:5422222222:4143212345
					 #1,002:0000200000000000:4:5
					 [출처] 데레시뮤 채보 제작방법|작성자 Van Azure
                     */
                    //data[0]: 0,000 thread#, block
                    //data[1]:20202220 type
                    //data[2]:54555 start
                    //data[3]:41441 end
					String [] tb=data[0].split(",");
					int thread= Integer.parseInt(tb[0]);
					int block= Integer.parseInt(tb[1]);
					char[] noteTypes=data[1].toCharArray();
					//1. count thd notes
					int cnt=0;
					List<Ball> balls=new ArrayList<>();
					char[] startPos=data[2].toCharArray();
					char[] endPos=data[3].toCharArray();
					/*if (cnt != startPos.length || cnt != endPos.length)
					{
						//Error
					}*/
					for (int i=0;i<noteTypes.length;i++)
					{
						char ch=noteTypes[i];
						Ball.BallType bt=Ball.BallType.Normal;
						if (ch != '0')
						{
							cnt++;
							switch(ch) {
                                case '1':
                                    bt = Ball.BallType.FlickLeft;
                                    break;
                                case '2':
                                    bt = Ball.BallType.Normal;
                                    break;
                                case '3':
                                    bt = Ball.BallType.FlickRight;
                                    break;
                                case '4':
                                    bt = Ball.BallType.LongUp;
                                    break;
                                case '5':
                                    bt = Ball.BallType.Slide;
                                    break;
                            }
							//start lane end lane
							//balls.add(new Ball(startPos[cnt-1]-'0',endPos[cnt-1]-'0',bt,(int)(block * bpm +i),thread));
						}
					}
                }
				else
				{
                    //meta data
                    String [] metadata=line.split("\\s");
                    //metadata[1] is data [0] is index
                    //expensive but convenient
                    final String d=metadata[1];
                    switch (metadata[0])
                    {
                        case "#Title":
                            title = d;
                            break;
                        case "#Lyricist":
                            lyricist = d;
                            break;
                        case "#Composer":
                            composer = d;
                            break;
                        case"#BackGround":
                            background = d;
                            break;
                        case "#Song":
                            song = d;
                            break;
                        case "#Lyrics":
                            lyrics = d;
                            break;
                        case "#BPM":
                            bpm = Double.parseDouble(d);
                            break;
                        case "#Offset":
                            offset = Integer.parseInt(d);
                            break;
                        case "#MovieOffset":
                            movieoffset = Integer.parseInt(d);
                            break;
                        case "#Difficulty":
                            difficulty = Difficulties.valueOf(d);
                            break;
                        case "#LV":
                            lv = Integer.parseInt(d);
                            break;
                        case "#BGMVol":
                            bgmvol = Integer.parseInt(d);
                            break;
                        case "#SEVol":
                            sevol = Integer.parseInt(d);
                            break;
                        case "#Attribute":
                            attribute = ColorType.valueOf(d);
                            break;

                    }
                }
                if (line.startsWith("#Title"))//キミと☆Are You Ready？
					//read one line
					line=br.readLine();
            }
        }
		catch (IOException e)
		{

        }
    }

    public void LoadNotemap2(File candidateFile)
	{
        isLoaded = true;
        File notemapFile = candidateFile;
        try
		{
            BufferedReader br = new BufferedReader(new FileReader(notemapFile));
            int bpm = -1;
            int block = -1;
            boolean lastWasBlock = false;
            try
			{
                //StringBuilder sb = new StringBuilder();
                String line = br.readLine();
                while (line != null)
				{
                    //use line here
                    if (line.startsWith("# "))
					{
                        if (bpm < 0)
                            throw new RuntimeException("#bpm must be specified at least once in the first block");
                        lastWasBlock = false;
                        line = line.replaceAll("# ", "").trim();
                        //[종류][시작 라인][플릭][다음 라인]
                        String[] notes = line.split("\\s");
                        Ball[] notesProcessed = new Ball[5];
                        for (int i = 0; i < notes.length; i++)
						{
                            notesProcessed[i] = getNote(notes[i]);
                        }
                        blocks.get(blocks.size() - 1).AddBalls(notesProcessed);
                    }
					else if (line.startsWith("#startframe"))
					{
                        lastWasBlock = false;
                        line = line.replaceAll("#startframe", "").trim();
                        try
						{
                            startframe = Integer.parseInt(line);
                        }
						catch (NumberFormatException e)
						{
                            throw new RuntimeException("#startframe error");
                        }
                    }
					else if (line.startsWith("#block")) //4박자를 얼마나 쪼개나 악보에서 느낌 16비트면 16
                    {
                        lastWasBlock = true;
                        line = line.replaceAll("#block", "").trim();
                        try
						{
                            block = Integer.parseInt(line);
                        }
						catch (NumberFormatException e)
						{
                            throw new RuntimeException("#block error");
                        }
                        blocks.add(new Block(block));
                    }
					else if (line.startsWith("#setbpm"))
					{
                        if (!lastWasBlock)
                            throw new RuntimeException("#setbpm must be right under the #block command");
                        lastWasBlock = false;
                        line = line.replaceAll("#setbpm", "").trim();
                        try
						{
                            bpm = Integer.parseInt(line);
                        }
						catch (NumberFormatException e)
						{
                            throw new RuntimeException("#setbpm error");
                        }
                    }
                    //end
                    //sb.append(System.lineSeparator());
                    line = br.readLine();
                }
                //String everything = sb.toString();
            }
			catch (IOException e)
			{

            }
			finally
			{
                br.close();
            }

        }
		catch (IOException e)
		{
            e.printStackTrace();
        }
    }

    private Ball getNote(String notes)
	{
        char[] chars = notes.toCharArray();
        Ball.BallType ballTyoe;
        int startLine;
        int nextLine;
        switch (chars[0])
		{
            case '-':
                return new Ball(0, 0,/*null*/Ball.BallType.Bomb);
            case '1':
                ballTyoe = Ball.BallType.Normal;
                break;
            case '2':
                ballTyoe = Ball.BallType.LongDown;
                break;
            case '3':
                ballTyoe = Ball.BallType.Slide;
                break;
            default:
                //???
                throw new RuntimeException();
                //break;
        }
        startLine = Character.getNumericValue(chars[1]);
        nextLine = Character.getNumericValue(chars[3]);
        boolean startOfFlick = false;
        switch (chars[2])
		{
            case '0':
                break;
            case '1':
                startOfFlick = true;
                ballTyoe = Ball.BallType.FlickLeft;
                break;
            case '2':
                startOfFlick = false;
                ballTyoe = Ball.BallType.FlickLeft;
                break;
            case '3':
                startOfFlick = true;
                ballTyoe = Ball.BallType.FlickRight;
                break;
            case '4':
                startOfFlick = false;
                ballTyoe = Ball.BallType.FlickRight;
                break;
        }
        Ball ball = new Ball(0, 0, ballTyoe);
        ball.startOfFlick = startOfFlick;
        return ball;
    }

    int startframe;
    List<Block> blocks = new ArrayList<>();

    public List<Ball> getBalls() {
        return balls;
    }

    //note form: list of balls?
    List<Ball> balls;
    File musicFile;
    boolean isLoaded;
    File dir;
    String songName = "?";

    List<List<File>> notemapFiles = new ArrayList<List<File>>(5);
    {
        for(int it = 0;it<5;it++)
        {
            notemapFiles.add(new ArrayList<File>());
        }
    }

    public String getName()
	{
        return songName;
    }
	public String getPath()
	{
		return musicFile.getPath();
	}
}
