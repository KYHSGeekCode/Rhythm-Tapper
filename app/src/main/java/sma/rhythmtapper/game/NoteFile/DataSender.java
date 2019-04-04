package sma.rhythmtapper.game.NoteFile;

public  class DataSender

{

    private static String songName, songLevel, pathBasic, folderName, beatmapPath, wavPath, mp3Path, oggPath, bgaPath, backPath;

    private static boolean autoPlay, noMusic, noBga, randWave, isMirror;

    private static int bgaFrame;

    private static float speedAmp;

    //private static GameDataArchiver data;

    private static NotemapMode notemapMode;

    private static GameMode gameMode;



    public static void GetSongData(String fd, String sN, String sL, String basePath, String beatmap, String wav, String mp3, String ogg, String bga, String back)

    {

        folderName = fd;

        songName = sN;

        songLevel = sL;

        pathBasic = basePath;

        beatmapPath = beatmap;

        wavPath = wav;

        mp3Path = mp3;

        oggPath = ogg;

        bgaPath = bga;

        backPath = back;

    }



    public static void GetGameOptionData(boolean aP, boolean nm, boolean nbga, boolean rW, boolean iM, float sA, int bf)

    {

        autoPlay = aP;

        noMusic = nm;

        noBga = nbga;

        randWave = rW;

        isMirror = iM;

        speedAmp = sA;

        bgaFrame = bf;

    }



    public static void SetNotemapMode(NotemapMode mode)

    {

        notemapMode = mode;



        if (mode.equals(NotemapMode.TW5) || mode.equals(NotemapMode.SSTrain) || mode.equals(NotemapMode.DelesteSimulator)) { gameMode = GameMode.Starlight; }

        else if (mode.equals(NotemapMode.TW2))

        {

            //if (PlayerPrefs.HasKey("theater2") && PlayerPrefs.GetInt("theater2").equals(0)) { gameMode = GameMode.Theater2P; }

            //else
             { gameMode = GameMode.Theater2L; }

        }

        else if (mode.equals(NotemapMode.TW4)) { gameMode = GameMode.Theater4; }

        else if (mode.equals(NotemapMode.TW6)) { gameMode = GameMode.Theater; }

        else if (mode.equals(NotemapMode.TW1)) { gameMode = GameMode.Platinum; }

    }



    //public static void SetGameResult(GameDataArchiver get)

    //{

      //  data = get;

    //}



    public static String ReturnSongName()

    {

        return songName;

    }



    public static boolean ReturnAutoPlay()

    {

        return autoPlay;

    }



    public static boolean ReturnMusicNotPlay()

    {

        return noMusic;

    }



    public static boolean ReturnNoBGA()

    {

        return noBga;

    }



    public static boolean ReturnMirror() { return isMirror; }



    public static boolean ReturnRandWave() { return randWave; }



    public static float ReturnSpeedAmp()

    {

        return speedAmp;

    }



    public static String ReturnMobilePath(NotemapMode mode)

    {

        return beatmapPath;

    }



    public static NotemapMode ReturnNotemapMode()

    {

        return notemapMode;

    }



    public static GameMode ReturnGameMode() { return gameMode; }



    public static String ReturnMp3Path()

    {

        return mp3Path;

    }



    public static String ReturnWavPath()

    {

        return wavPath;

    }



    public static String ReturnOggPath()

    {

        return oggPath;

    }



    public static String ReturnSongFolderPath() { return pathBasic + folderName; }



    public static String ReturnBasePath()

    {

        return pathBasic + folderName + "/" + songName;

    }



    public static int ReturnBGAFrame()

    {

        return bgaFrame;

    }



    public static String ReturnBGAPath() { return bgaPath; }



    public static String ReturnBackImgPath() { return backPath; }


//
//    public static GameDataArchiver ReturnGameResult()
//
//    {

  //      return data;

    //}



    public static float DivideBetweenTwoPos(float left, float right, float m, float n)

    {

        return (m * right + n * left) / (m + n);

    }


/*
    public static void ResultPopOut(out String sL, out String fF, out boolean aP, out boolean rW, out boolean iM, out String sA)

    {

        sL = songLevel.ToUpper();

        fF = notemapMode.ToString();

        aP = autoPlay;

        rW = randWave;

        iM = isMirror;

        sA = speedAmp.ToString("N1");

    }
*/
}
