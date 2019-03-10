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
import sma.rhythmtapper.game.NoteFile.*;

public class SongListViewAdapter extends BaseAdapter //implements ListView.OnScrollListener
{
	public static final int INSERT_COUNT=80;

	private String TAG="Disassembler LV";

	//private MainActivity mainactivity;

	List<NoteFile> items=new ArrayList<>();
	public void Clear()
	{
		items.clear();
	}
	public void addAll(ArrayList<NoteFile> list)
	{
		items=list;
		notifyDataSetChanged();

	}
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
            convertView = inflater.inflate(R.layout.row_songs,parent, false);		
        }
		
        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
		TextView tvTitle=(TextView) convertView.findViewById(R.id.rowsongsTextViewSongName);
		Button btEasy=(Button) convertView.findViewById(R.id.rowsongsButtonEasy);
		
		
			NoteFile listViewItem = (NoteFile) getItem(position);//listViewItemList/*[position];*/.get(position);
			
			 //  iconImageView.setImageDrawable(listViewItem.getIcon());
		
        return convertView;
    }

	// Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
	@Override
	public int getCount()
	{
		return items.size();//listViewItemList.size();// lvLength;//listViewItemList//size() ;
	}

    @Override
    public Object getItem(int position)
	{
		Long addrl=address.get(position);
		if(addrl==null)
			return new ListViewItem();//? FIXME. crashes when rotated screen here, NPE.
		long addr=addrl.longValue();
		ListViewItem lvi=itemsNew.get(addr);
		if(lvi==null)
		{
			LoadMore(position,addr);
		}
        return lvi;
    }

    public void addItem(ListViewItem item)
	{
        itemsNew.put(item.disasmResult.address,item);
		address.put(writep,new Long(item.disasmResult.address));
		writep++;//continuously add
		//notifyDataSetChanged();
    }

	public void OnJumpTo(/*int position,*/long address)
	{
		//refreshing is inevitable, and backward is ignored.
		//cause: useless
		//however will implement backStack
		this.address.clear();
		LoadMore(/**/0,address);
		currentAddress=address;
	}
	/*
	 public void addAll(ArrayList/*LongSparseArra <ListViewItem> data)
	 {
	 listViewItemList.addAll(data);
	 notifyDataSetChanged();
	 }
	 //You should not modify
	 public ArrayList<ListViewItem> itemList()
	 {
	 return listViewItemList;//new ArrayList<ListViewItem>().addAll(listViewItemList);
	 }

     // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
	 @Override
	 public int getCount()
	 {
	 return listViewItemList.size();// lvLength;//listViewItemList//size() ;
	 }

	 @Override
	 public Object getItem(int position)
	 {
	 return listViewItemList.get(position) ;
	 }

	 public void addItem(ListViewItem item)
	 {
	 listViewItemList.add(item);
	 //notifyDataSetChanged();
	 }
	 */

	//?!!!
    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
    @Override
    public long getItemId(int position)
	{
        return position;
    }

	public void addItem(DisasmResult disasm)
	{
        ListViewItem item = new ListViewItem(disasm);
        addItem(item);
		//notifyDataSetChanged();
    }
	ColorHelper colorHelper;
	private int architecture;

    public ListViewAdapter(AbstractFile file,ColorHelper ch,MainActivity ma)
	{
		this.file=file;
		colorHelper=ch;
		architecture=0;//FIXME:clarification needed but OK now
		//address=//new long[file.fileContents.length];//Use sparseArray if oom
		mainactivity=ma;
    }

	public void setArchitecture(int architecture)
	{
		this.architecture = architecture;
	}

	public int getArchitecture()
	{
		return architecture;
	}
	//https://stackoverflow.com/a/48351453/8614565
	public static int convertDpToPixel(float dp){
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return Math.round(px);
    }
	public int dp180=convertDpToPixel(180);
	public int dp260=convertDpToPixel(260);
}
//http://recipes4dev.tistory.com/m/43
