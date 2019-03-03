package sma.rhythmtapper.game.models;

public enum Difficulties {
    EASY,
    NORMAL,
    HARD,
    MASTER,
    MASTERPLUS;
    public String getFileName()
    {
        switch (this)
        {
            case EASY:
                return "easy";
            case NORMAL:
                return "normal";
            case HARD:
                return "hard";
            case MASTER:
                return "apex";
            case MASTERPLUS:
                return "master+";
            default:
                return "hard";
        }
    }
}
