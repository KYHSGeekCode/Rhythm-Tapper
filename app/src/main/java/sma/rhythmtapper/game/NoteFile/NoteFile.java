package sma.rhythmtapper.game.NoteFile;

import java.io.*;
import java.util.*;
import sma.rhythmtapper.game.models.*;

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
        String songName;
        String artist;
        int difficulties[]=new int[5];

        try
        {
            BufferedReader br= new BufferedReader(new FileReader(infoFile));
            String line=br.readLine();
            while(line !=null)
            {
                if(line.startsWith("#title"))
                {
                    line=line.replace("#title ","").trim();
                    songName=line;
                    continue;
                }
                if(line.startsWith("#artist"))
                {
                    line=line.replace("#artist ","").trim();
                    artist=line;
                    continue;
                }
                if(line.startsWith("#easy"))
                {
                    difficulties[0] = Integer.parseInt(line.replace("#easy ","").trim());
                    continue;
                }
                if(line.startsWith("#normal"))
                {
                    difficulties[1] = Integer.parseInt(line.replace("#normal ","").trim());
                    continue;
                }
                if(line.startsWith("#hard"))
                {
                    difficulties[2] = Integer.parseInt(line.replace("#hard ","").trim());
                    continue;
                }
                if(line.startsWith("#master"))
                {
                    difficulties[3] = Integer.parseInt(line.replace("#master ","").trim());
                    continue;
                }
                if(line.startsWith("#apex"))
                {
                    difficulties[4] = Integer.parseInt(line.replace("#apex ","").trim());
                    continue;
                }
                line=br.readLine();
            }
        } catch (IOException | NumberFormatException e)
        {
            throw new RuntimeException(e);
        }
        //info.txt parse done.
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
						//[종류][시작 라인][플릭][다음 라인]
						String [] notes = line.split("\\s");
						Ball [] notesProcessed=new Ball[5];
						for(int i=0;i<notes.length;i++){
							notesProcessed[i]=getNote(notes[i]);
						}
                       blocks.get(blocks.size()-1).AddBalls(notesProcessed);
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
                        blocks.add(new Block(block));
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

	private Ball getNote(String notes)
	{
		char[] chars=notes.toCharArray();
		Ball.BallType ballTyoe;
		int startLine;
		int nextLine;
		switch(chars[0])
		{
			case '-':
				return new Ball(0,0,/*null*/Ball.BallType.Bomb);
			case '1':
				ballTyoe=Ball.BallType.Normal;
				break;
			case '2':
				ballTyoe=Ball.BallType.LongDown;
				break;
			case '3':
				ballTyoe=Ball.BallType.Slide;
				break;
			default:
				//???
				throw new RuntimeException();
				//break;
		}
		startLine=Character.getNumericValue(chars[1]);
		nextLine=Character.getNumericValue(chars[3]);
		boolean startOfFlick=false;
		switch(chars[2])
		{
			case '0':
				break;
			case '1':
				startOfFlick=true;
				ballTyoe=Ball.BallType.FlickLeft;
				break;
			case '2':
				startOfFlick=false;
				ballTyoe=Ball.BallType.FlickLeft;
				break;
			case '3':
				startOfFlick=true;
				ballTyoe=Ball.BallType.FlickRight;
				break;
			case '4':
				startOfFlick=false;
				ballTyoe=Ball.BallType.FlickRight;
				break;
		}
		Ball ball=new Ball(0,0,ballTyoe);
		ball.startOfFlick=startOfFlick;
		return ball;
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
