package sma.rhythmtapper.game.NoteFile;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import sma.rhythmtapper.game.models.Ball;

public class Block implements Serializable {

    int bits;
    List<List<Ball>> balls = new ArrayList<>();

    public Block(int bits) {
        for (int i = 0; i < 5; i++) {
            balls.add(new ArrayList<Ball>());
        }
        reader = 0;
        this.bits = bits;
    }

    //lane : 1~5
    public void AddBall(Ball ball, int lane) {
        balls.get(lane - 1).add(ball);
    }

    public void AddBalls(Ball[] balls) {
        for (int i = 0; i < 5; i++) {
            this.balls.get(i).add(balls[i]);
        }
        if (this.balls.get(0).size() > bits)
            throw new RuntimeException("more notes than that of the #block statement");
    }

    public Ball getSpawnBall(int lane) {
        return balls.get(lane).get(reader);
    }

    public boolean NextLine() {
        reader++;
        return reader < balls.size();
    }

    int reader;
    //thread thread
}

/*
public class DelesteBlockData
{
    public List<String> DataLines;
    public List<Integer> Channel;
    public List<Byte[]> Color;
    public List<Double> Speed;
    public int Measure;
    public int BeatPer4;
    public double BPM;
    public boolean IsAdded;

    public DelesteBlockData(int BlockID)
    {
        DataLines = new ArrayList<>();
        Channel = new ArrayList<>();
        Color = new ArrayList<>(4);
        Speed = new ArrayList<>();
        IsAdded = false;
        BeatPer4 = 4;
        Measure = BlockID;
    }

    // Translated from Tempest-Wave/DelesteData.cs
    //  @Parse a block with the given ID.
    //  @param ID the ID of the block
    //  @param Time the time of the start of the block
    //  @StaticData the data for the entire file
    public void ParseBlock(/*GameManager Game, //*ref/ int ID, /*ref/ double Time, DelesteGlobalData StaticData)
    {
        int MaxBit = 1;
        List<Integer> DataIndex = new ArrayList<>(), StartIndex = new ArrayList<>(), EndIndex = new ArrayList<>();
        ArrayList<String[]> Datas = new ArrayList<>();

        // DataIndex, StartIndex, EndIndex의 초기화와 MaxBit의 갱신, 그리고 Datas의 값 대입을 수행합니다.
        for(int i = 0; i < DataLines.size(); i++)
        {
            Datas.add(DataLines.get(i).split(":" ));
            DataIndex.add(-1);
            StartIndex.add(0);
            EndIndex.add(0);
            MaxBit = LCM(MaxBit, Datas.get(i)[1].length());
        }

        // MaxBit를 기반으로, 차례차례 데이터를 해석하고 추가하는 전체 과정입니다.
        for(int i = 0; i < MaxBit; i++)
        {
            // 선제적으로, 각종 큐들을 살펴보면서 조건을 만족한다면 데이터를 그에 맞춥니다.
            if(StaticData.Measure.size() > 0 && StaticData.MeasurePos.get(0) <= Measure + (i / (double)MaxBit))
            {
                StaticData.BeatMultiplier = StaticData.Measure.get(0);
                StaticData.Measure.remove(0);
                StaticData.MeasurePos.remove(0);
            }
            if(StaticData.ChangeBPM.size() > 0 && StaticData.ChangeBPMPos.get(0) <= Measure + (i / (double)MaxBit))
            {
                StaticData.CurrentBPM = StaticData.ChangeBPM.get(0);
                StaticData.ChangeBPM.remove(0);
                StaticData.ChangeBPMPos.remove(0);
            }
            if(StaticData.HS2.size() > 0 && StaticData.HS2Pos.get(0) <= Measure + (i / (double)MaxBit))
            {
                StaticData.SpeedMultiplier = StaticData.HS2.get(0);
                StaticData.HS2.remove(0);
                StaticData.HS2Pos.remove(0);
            }
            if(StaticData.Delay.size() > 0 && StaticData.DelayPos.get(0) <= Measure + (i / (double)MaxBit))
            {
                Time += StaticData.Delay.get(0);
                StaticData.Delay.remove(0);
                StaticData.DelayPos.remove(0);
            }
            if(StaticData.Scroll.size() > 0 && StaticData.ScrollPos.get(0) <= Measure + (i / (double)MaxBit))
            {
                /*Game.Dispensor./CreateNote(ID++, 0, android.graphics.Color.argb(255, 255, 255, 0), NoteInfo.SystemNoteScroller, FlickMode.None, (float)Time * 60, StaticData.Scroll.get(0)[0].floatValue(), 0, 0, new ArrayList<Integer>());
                StaticData.EndScrollTime = Time + StaticData.Scroll.get(0)[1];
                StaticData.IsScrollModified = true;
                StaticData.Scroll.remove(0);
                StaticData.ScrollPos.remove(0);
            }
            if(StaticData.EndScrollTime <= Time && StaticData.IsScrollModified)
            {
                /*Game.Dispensor./CreateNote(ID++, 0, android.graphics.Color.argb(255, 255, 255, 0), NoteInfo.SystemNoteScroller, FlickMode.None, (float)Time * 60, 1, 0, 0, new ArrayList<Integer>());
                StaticData.IsScrollModified = false;
            }

            // Data를 병렬적으로 처리합니다.
            for (int j = 0; j < Datas.size(); j++)
            {
                // 만약 현재의 i 지점에 해당하는 해당 데이터의 계산된 인덱스가 기존의 인덱스보다 크다면 값을 그에 맞춥니다.
                if (i / (MaxBit / Datas.get(j)[1].length()) > DataIndex.get(j)) { DataIndex.set(j,DataIndex.get(j)+1); }
                else { continue; }

                // 데이터를 가져옵니다. 코드 간략화를 위함입니다.
                char Command = Datas.get(j)[1].charAt(DataIndex.get(j));

                // 필요한 개별 변수를 설정합니다. (1차)
                NoteInfo Mode;
                FlickMode Flick;

                // 노트의 기본 정보를 해석합니다.
                if (Command==('1') || Character.toUpperCase(Command)=='L')
                {
                    Mode = NoteInfo.NormalNote;
                    Flick = DataSender.ReturnMirror() ? FlickMode.Right : FlickMode.Left; // 미러 모드 적용 코드입니다.
                }
                else if (Command=='2' || Character.toUpperCase(Command)=='T')
                {
                    Mode = NoteInfo.NormalNote;
                    Flick = FlickMode.None;
                }
                else if (Command=='3' || Character.toUpperCase(Command)=='R')
                {
                    Mode = NoteInfo.NormalNote;
                    Flick = DataSender.ReturnMirror() ? FlickMode.Left : FlickMode.Right; // 미러 모드 적용 코드입니다.
                }
                else if (Command=='4' || Character.toUpperCase(Command)=='H')
                {
                    Mode = NoteInfo.LongNoteStart;
                    Flick = FlickMode.None;
                }
                else if (Command=='5' || Character.toUpperCase(Command)=='S')
                {
                    Mode = NoteInfo.SlideNoteStart;
                    Flick = FlickMode.None;
                }
                else { continue; }

                // 필요한 개별 변수를 설정합니다. (2차)
                double Start = 3, End = 3;

                // 시작 지점과 끝 지점을 설정합니다.
                // 위 코드의 continue 때문에, 유효한 노트 데이터가 해석이 되어야 이 부분부터의 코드에 도달합니다.
                if (Datas.get(j).length >= 3)
                {
                    int DummyStart;
                    char StartText = Datas.get(j)[2].charAt(StartIndex.get(j));
                    if(Character.isDigit(StartText))
                    {
                        Start=StartText-'0';
                    }//if (int.TryParse(StartText.ToString(), DummyStart)) { Start = DummyStart; }
                    else
                    {
                        if (Character.toUpperCase(StartText)=='A') { Start = 1.5; }
                        else if (Character.toUpperCase(StartText)=='B') { Start = 2.5; }
                        else if (Character.toUpperCase(StartText)=='C') { Start = 3.5; }
                        else if (Character.toUpperCase(StartText)=='D') { Start = 4.5; }
                        else { throw new RuntimeException("Error in start point parsing. Please check your beatmap."); }
                    }
                    StartIndex.set(j,StartIndex.get(j)+1);
                    if (Datas.get(j).length==3) { End = Start; }
                }
                if(Datas.get(j).length >= 4)
                {
                    int DummyEnd;
                    char EndText = Datas.get(j)[3].charAt(EndIndex.get(j));
                    if(Character.isDigit(EndText))
                    {
                        DummyEnd=EndText-'0';
                    //}
                    //if (int.TryParse(EndText.ToString(), DummyEnd))
                    //{
                        End = DummyEnd;
                        if (DataSender.ReturnMirror()) { End = 6 - End; } // 미러 모드 적용 코드입니다.
                    }
                    else
                    {
                        if (Character.toUpperCase(EndText)=='A') { End = 1.5; }
                        else if (Character.toUpperCase(EndText)=='B') { End = 2.5; }
                        else if (Character.toUpperCase(EndText)=='C') { End = 3.5; }
                        else if (Character.toUpperCase(EndText)=='D') { End = 4.5; }
                        else { throw new RuntimeException("Error in end point parsing. Please check your beatmap."); }
                    }
                    EndIndex.set(j,EndIndex.get(j)+1);
                }

                // 필요한 개별 변수를 설정합니다. (3차)
                List<Integer> Prevs = new ArrayList<>();
                if (!StaticData.HoldPrev.containsKey(End)) { StaticData.HoldPrev.put(End, 0); }
                if (!StaticData.TailPrev.containsKey(Channel.get(j))) { StaticData.TailPrev.put(Channel.get(j), 0); }
                if (!StaticData.ConnectorPrev.containsKey(Channel.get(j))) { StaticData.ConnectorPrev.put(Channel.get(j), new ArrayList<Double[]>(3)); }
                if (!StaticData.BeforeTime.containsKey(Channel.get(j))) { StaticData.BeforeTime.put(Channel.get(j), 0.0); }

                // 연결할 이전 노트가 있으면 연결합니다. Tail, Connector 순서로 연결합니다.

                // Tail은 홀드 노트를 우선으로 합니다 (추후 변경 가능). 중복은 허용하지 않습니다.
                if (Mode.equals(NoteInfo.NormalNote) && StaticData.HoldPrev.get(End) > 0)
                {
                    Mode = NoteInfo.LongNoteEnd;
                    Prevs.add(StaticData.HoldPrev.get(End));
                    StaticData.HoldPrev.put(End , 0);
                }
                else if(Mode.equals(NoteInfo.NormalNote) && StaticData.TailPrev.get(Channel.get(j)) > 0)
                {
                    Mode = NoteInfo.SlideNoteEnd;
                    Prevs.add(StaticData.TailPrev.get(Channel.get(j)));
                    StaticData.TailPrev.put(Channel.get(j),  0);
                }
                if(Mode.equals(NoteInfo.SlideNoteStart) && StaticData.TailPrev.get(Channel.get(j)) > 0)
                {
                    Mode = NoteInfo.SlideNoteCheckpoint;
                    Prevs.add(StaticData.TailPrev.get(Channel.get(j)));
                }

                // Connector를 연결합니다.
                // ConnectorPrev[Channel[j]][x]에서 x=0의 값은 방향(증가 방향이 양수), x=1의 값은 비교 지점, x=2의 값은 노트 ID입니다.
                if (!Flick.equals(FlickMode.None) && StaticData.ConnectorPrev.get(Channel.get(j)).size() > 0)
                {
                    for (int k = 0; k < StaticData.ConnectorPrev.get(Channel.get(j)).size(); k++)
                    {
                        if (Time - StaticData.BeforeTime.get(Channel.get(j)) > 0 && Time - StaticData.BeforeTime.get(Channel.get(j)) <= 1)
                        {
                            if (StaticData.ConnectorPrev.get(Channel.get(j)).get(k)[0] > 0)
                            {
                                if (End > StaticData.ConnectorPrev.get(Channel.get(j)).get(k)[1])
                                {
                                    Prevs.add((StaticData.ConnectorPrev.get(Channel.get(j)).get(k)[2]).intValue());
                                }
                            }
                            else if (StaticData.ConnectorPrev.get(Channel.get(j)).get(k)[0] < 0)
                            {
                                if (End < StaticData.ConnectorPrev.get(Channel.get(j)).get(k)[1])
                                {
                                    Prevs.add((StaticData.ConnectorPrev.get(Channel.get(j)).get(k)[2]).intValue());
                                }
                            }
                            else { Prevs.add((StaticData.ConnectorPrev.get(Channel.get(j)).get(k)[2]).intValue()); }
                        }
                    }
                    if (StaticData.BeforeTime.get(Channel.get(j)) != Time)
                    {
                        StaticData.ConnectorPrev.get(Channel.get(j)).clear();
                    }
                }
                else if (Flick.equals(FlickMode.None) && StaticData.ConnectorPrev.get(Channel.get(j)).size() > 0 && StaticData.BeforeTime.get(Channel.get(j)) != Time) { StaticData.ConnectorPrev.get(Channel.get(j)).clear(); }

                // 다음 노트와 연결될 노트에 대한 데이터를 전역 데이터에 입력합니다.
                if (Mode.equals(NoteInfo.LongNoteStart)) { StaticData.HoldPrev.put(End, ID); }
                else if (Mode.equals(NoteInfo.SlideNoteStart) || Mode.equals(NoteInfo.SlideNoteCheckpoint)) { StaticData.TailPrev.put(Channel.get(j), ID); }
                if(!Flick.equals(FlickMode.None))
                {
                    if (Channel.get(j) % 4 == 0 || Channel.get(j) % 4 == 1)
                    {
                        if (Flick.equals(FlickMode.Left)) { StaticData.ConnectorPrev.get(Channel.get(j)).add(new Double[] { -1.0, End, (double)ID }); }
                        else if (Flick.equals(FlickMode.Right)) { StaticData.ConnectorPrev.get(Channel.get(j)).add(new Double[] { 1.0, End, (double)ID }); }
                    }
                    else if (Channel.get(j) % 4 == 2 || Channel.get(j) % 4 == 3) { StaticData.ConnectorPrev.get(Channel.get(j)).add(new Double[] { 0.0, End, (double)ID }); }
                    StaticData.BeforeTime.put(Channel.get(j),  Time);
                }

                if (DataSender.ReturnRandWave()) { Start = ThreadLocalRandom.current().nextDouble(1, 6); }
                if (DataSender.ReturnMirror()) { Start = 6 - Start; }

                // 노트를 게임에 추가합니다. 이 때 시간을 프레임으로 보정합니다 (60배수).
                /*Game.Dispensor./CreateNote(ID++, 1, android.graphics.Color.argb(Color.get(j)[0], Color.get(j)[1], Color.get(j)[2], Color.get(j)[3]), Mode, Flick, (float)Time * 60, (float)(Speed.get(j) * StaticData.SpeedMultiplier), DataSender.ReturnRandWave() ? (float)ThreadLocalRandom.current().nextDouble(1, 6) : (float)Start, (float)End, Prevs);
            }

            Time += (((240 / StaticData.CurrentBPM) * StaticData.BeatMultiplier) / MaxBit);
        }
    }

    public void CreateNote(int ID, int size, int color, NoteInfo mode, FlickMode flick, float time, float speed, float start, float end, List<Integer> prevs)
    {

    }
    public int LCM(int a, int b)
    {
        return (a * b) / GCD(a, b);
    }

    public int GCD(int a, int b)
    {
        if (b==0) { return a; }
        return GCD(b, a % b);
    }*/
//}
