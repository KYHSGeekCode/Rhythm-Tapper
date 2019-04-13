package sma.rhythmtapper;

import android.content.*;
import android.graphics.*;
import android.text.*;
import android.text.style.*;
import android.view.*;
import android.widget.*;

import java.util.*;

import android.util.*;
import android.content.res.*;

import sma.rhythmtapper.framework.Game;
import sma.rhythmtapper.game.GameScreen;
import sma.rhythmtapper.game.LoadingScreen;
import sma.rhythmtapper.game.NoteFile.*;
import sma.rhythmtapper.game.models.Difficulties;
import sma.rhythmtapper.models.Difficulty;

public class SongListViewAdapter extends BaseAdapter implements View.OnClickListener//implements ListView.OnScrollListener
{
    public static final int INSERT_COUNT = 80;
    //private final Game game;
    private final Context context;

    private String TAG = "Disassembler LV";

    //private MainActivity mainactivity;

    List<NoteFile> items = new ArrayList<>();

    public void Clear()
	{
        items.clear();
    }

    /*
	 public void addAll(ArrayList<NoteFile> list) {
	 items = list;
	 notifyDataSetChanged();

	 }
	 */
    //You should not modify
    public List<NoteFile> itemList()
	{
        return items;///*listViewItemList;//*/new ArrayList<ListViewItem>().addAll(listViewItemList);
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
	{
        // final int pos = position;
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null)
		{
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row_songs, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView tvTitle = (TextView) convertView.findViewById(R.id.rowsongsTextViewSongName);
        Button btEasy = (Button) convertView.findViewById(R.id.rowsongsButtonEasy);
        Button btNormal = (Button) convertView.findViewById(R.id.rowsongsButtonRegular);
        Button btHard = (Button) convertView.findViewById(R.id.rowsongsButtonHard);
        Button btMaster = (Button) convertView.findViewById(R.id.rowsongsButtonMaster);
        Button btApex = (Button) convertView.findViewById(R.id.rowsongsButtonApex);

        NoteFile item = (NoteFile) getItem(position);
        tvTitle.setText(item.getName());
        btEasy.setOnClickListener(this);
		btNormal.setOnClickListener(this);
		btHard.setOnClickListener(this);
		btMaster.setOnClickListener(this);
		btApex.setOnClickListener(this);

		btEasy.setTag(item);
		btNormal.setTag(item);
		btHard.setTag(item);
		btMaster.setTag(item);
		btApex.setTag(item);
        //listViewItemList/*[position];*/.get(position);

        //  iconImageView.setImageDrawable(listViewItem.getIcon());

        return convertView;
    }

    public void addItem(NoteFile item)
	{
        items.add(item);
        notifyDataSetChanged();
    }

    public void addAll(ArrayList<NoteFile> items)
	{
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    @Override
    public int getCount()
	{
        return items.size();// lvLength;//listViewItemList//size() ;
    }

    @Override
    public Object getItem(int position)
	{
        return items.get(position);
    }

    //?!!!
    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
    @Override
    public long getItemId(int position)
	{
        return position;
    }

    public SongListViewAdapter(Context context)
	{
        this.context = context;
    }

    //https://stackoverflow.com/a/48351453/8614565
    public static int convertDpToPixel(float dp)
	{
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return Math.round(px);
    }

    public int dp180 = convertDpToPixel(180);
    public int dp260 = convertDpToPixel(260);

    @Override
    public void onClick(View view)
	{
        Difficulties diff = Difficulties.EASY;
        switch (view.getId())
		{
            case R.id.rowsongsButtonEasy:
                diff = Difficulties.EASY;
                break;
            case R.id.rowsongsButtonRegular:
                diff = Difficulties.NORMAL;
                break;
            case R.id.rowsongsButtonHard:
                diff = Difficulties.HARD;
                break;
            case R.id.rowsongsButtonMaster:
                diff = Difficulties.MASTER;
                break;
            case R.id.rowsongsButtonApex:
                diff = Difficulties.MASTERPLUS;
                break;
				//game.setScreen(new LoadingScreen(game, new Difficulty(Difficulties.EASY,(String)view.getTag(),0,0)));
        }
		NoteFile nf=(NoteFile)view.getTag();
		try
		{
			nf.Load(diff);
		}
		catch (RuntimeException e)
		{
			Toast.makeText(context,"No such level",Toast.LENGTH_SHORT).show();
			return;
		}
        Intent i = new Intent(context, ChooseGuestActivity.class);
        // add flag, when activity already runs,
        // use it instead of launching a new instance
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		i.putExtra("notefile", nf);
        i.putExtra("Difficulty", diff.name());
        //i.putExtra("Name", (String) view.getTag());
        //i.putExtra("bpm",(String));
        context.startActivity(i);
    }
}

