package sma.rhythmtapper.game.NoteFile;

import java.util.ArrayList;
import java.util.HashMap;

public class DelesteGlobalData {
    public double CurrentBPM;
    public double SpeedMultiplier;
    public double BeatMultiplier;
    public double EndScrollTime;
    public boolean IsScrollModified;

    public ArrayList<Double> ChangeBPM = new ArrayList<Double>(), ChangeBPMPos = new ArrayList<Double>();
    public ArrayList<Double> Measure = new ArrayList<Double>(), MeasurePos = new ArrayList<Double>();
    public ArrayList<Double> HS2 = new ArrayList<Double>(), HS2Pos = new ArrayList<Double>();
    public ArrayList<Double> Delay = new ArrayList<Double>(), DelayPos = new ArrayList<Double>();
    public ArrayList<Double[]> Scroll = new ArrayList<Double[]>(2);
    public ArrayList<Double> ScrollPos = new ArrayList<Double>();
    public HashMap<Integer, ArrayList<Double[]>> ConnectorPrev = new HashMap<Integer, ArrayList<Double[]>>(3);
    public HashMap<Integer, Double> BeforeTime = new HashMap<Integer, Double>();
    public HashMap<Integer, Integer> TailPrev = new HashMap<Integer, Integer>();
    public HashMap<Double, Integer> HoldPrev = new HashMap<Double, Integer>(), BeforeFlickData = new HashMap<Double, Integer>();

    public DelesteGlobalData() {
        BeatMultiplier = 1;
        SpeedMultiplier = 1;
        EndScrollTime = 0;
        IsScrollModified = false;
    }
}