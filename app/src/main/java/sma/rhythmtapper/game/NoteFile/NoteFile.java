package sma.rhythmtapper.game.NoteFile;

import java.io.*;
import java.util.*;
import sma.rhythmtapper.game.models.*;
import sma.rhythmtapper.models.Difficulty;

public class NoteFile
{
    //dir should end with /
	public NoteFile(File dir)
	{
	    this.dir=dir;
	    isLoaded=false;
		//file is the root directory of the project
		//file/*.mp3,wav
		//file/easy.notemap2
		//file/normal.notemap2
		//file/hard.notemap2
		//file/master.notemap2
		//file/master+.notemap2
		File[] files=dir.listFiles();
		for(File file:files)
		{
			String filename=file.getName();
			String extension = filename.substring(filename.lastIndexOf("."));
			if(extension.compareToIgnoreCase("mp3")==0||extension.compareToIgnoreCase("wav")==0)
			{
				musicFile=file;
				break;
			}
		}
		File infoFile=new File(dir,"info.txt");
		//parse info.txt
	}

	public void Load(Difficulties difficulty)
    {
        isLoaded=true;
        File notemapFile=new File(dir,songName+"_"+difficulty.getFileName()+".notemap2");
        try {
            BufferedReader br = new BufferedReader(new FileReader(notemapFile));
            int bpm=-1;
            int block=-1;
            boolean lastWasBlock=false;
            try {
                //StringBuilder sb = new StringBuilder();
                String line = br.readLine();
                while (line != null) {
                    //use line here
                    if(line.startsWith("# "))
                    {
                        if(bpm<0)
                            throw new RuntimeException("#bpm must be specified at least once in the first block");
                        lastWasBlock=false;
                        line=line.replaceAll("# ","").trim();
//                        blocks.get(blocks.size()-1).AddBall(ball,lane);
                    } else if(line.startsWith("#startframe"))
                    {
                        lastWasBlock=false;
                        line=line.replaceAll("#startframe","").trim();
                        try{
                            startframe=Integer.parseInt(line);
                        } catch(NumberFormatException e)
                        {
                            throw new RuntimeException("#startframe error");
                        }
                    } else if (line.startsWith("#block")) //4박자를 얼마나 쪼개나 악보에서 느낌 16비트면 16
                    {
                        lastWasBlock=true;
                        line=line.replaceAll("#block","").trim();
                        try{
                            block=Integer.parseInt(line);
                        } catch(NumberFormatException e)
                        {
                            throw new RuntimeException("#block error");
                        }
                        blocks.add(new Block());
                    } else if (line.startsWith("#setbpm"))
                    {
                        if(!lastWasBlock)
                            throw new RuntimeException("#setbpm must be right under the #block command");
                        lastWasBlock=false;
                        line=line.replaceAll("#setbpm","").trim();
                        try{
                            bpm=Integer.parseInt(line);
                        } catch(NumberFormatException e)
                        {
                            throw new RuntimeException("#setbpm error");
                        }
                    }
                    //end
                    //sb.append(System.lineSeparator());
                    line = br.readLine();
                }
                //String everything = sb.toString();
            } catch(IOException e) {

            }finally
            {
                br.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    int startframe;
	List<Block> blocks=new ArrayList<>();
	//note form: list of balls?
	List<List<Ball>> balls;
	File musicFile;
	boolean isLoaded;
	File dir;
	String songName;
}
