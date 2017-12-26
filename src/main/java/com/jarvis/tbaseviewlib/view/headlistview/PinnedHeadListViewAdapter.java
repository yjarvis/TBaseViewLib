package com.jarvis.tbaseviewlib.view.headlistview;


//public class PinnedHeadListViewAdapter extends BaseAdapter implements OnScrollListener, PinnedHeaderAdapter {
//	/** 全部数据 */
//	private ArrayList<String> arrayList;
//	/** 头布局在集合中的位置 */
//	private ArrayList<Integer> arrayListPosition;
//	private Context context;
//
//	public PinnedHeadListViewAdapter(Context context, ArrayList<String> arrayList, ArrayList<Integer> arrayListPosition) {
//		this.context = context;
//		this.arrayList = arrayList;
//		this.arrayListPosition = arrayListPosition;
//	}
//
//	@Override
//	public int getCount() {
//		// TODO Auto-generated method stub
//		return arrayList.size();
//	}
//
//	@Override
//	public Object getItem(int position) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public long getItemId(int position) {
//		// TODO Auto-generated method stub
//		return 0;
//	}
//
//	@Override
//	public View getView(int position, View convertView, ViewGroup parent) {
//		HolderView holderView;
//		if (convertView == null) {
//			convertView = LayoutInflater.from(context).inflate(R.layout.fragment_book_listitem, null);
//			holderView = new HolderView();
//			holderView.headItem = (TextView) convertView.findViewById(R.id.book_list_head_item);
//			holderView.icon = (ImageView) convertView.findViewById(R.id.book_list_icon);
//			holderView.name = (TextView) convertView.findViewById(R.id.book_list_name);
//			holderView.duty = (TextView) convertView.findViewById(R.id.book_list_duty);
//			holderView.mobile = (TextView) convertView.findViewById(R.id.book_list_mobile);
//			holderView.company = (TextView) convertView.findViewById(R.id.book_list_company);
//			convertView.setTag(holderView);
//		} else {
//			holderView = (HolderView) convertView.getTag();
//		}
//		// 得到当前的头布局位置
//		int section = getCurrentPosition(position);
//		// 如果是需要显示头布局的Item，那么就多显示一个组件
//		if (getNextPosition(section) == position) {
//			holderView.headItem.setVisibility(View.VISIBLE);
//			holderView.headItem.setText(arrayList.get(position).toString());
//		} else {
//			holderView.headItem.setVisibility(View.GONE);
//		}
//
////		holderView.name.setText(arrayList.get(position).toString());
//
//		return convertView;
//	}
//
//	private class HolderView {
//		TextView headItem;
//		ImageView icon;
//		TextView name;
//		TextView duty;
//		TextView mobile;
//		TextView company;
//
//	}
//
//	// --------------------------------------------------以下OnScrollListener的监听--------------------------------------------------------------------------
//
//	@Override
//	public void onScrollStateChanged(AbsListView view, int scrollState) {
//	}
//
//	// 滑动的时候
//	@Override
//	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//		// view是否是PinnedHeaderListView的实例对象,如果是true,则进行配置
//		if (view instanceof PinnedHeaderListView) {
//			((PinnedHeaderListView) view).configureHeaderView(firstVisibleItem);
//		}
//	}
//
//	// ---------------------------------------------------以上OnScrollListener的监听-------------------------------------------------------------------------
//
//	// ---------------------------------------------------以下PinnedHeadListViewAdapter的回调-------------------------------------------------------------------------
//	@Override
//	public int getPinnedHeaderState(int position) {
//		int realPosition = position;
//		if (realPosition < 0 || position >= getCount()) {
//			return PINNED_HEADER_GONE;
//		}
//
//		// 通过真实的items位置，得到头布局的位置
//		int section = getCurrentPosition(realPosition);
//		// 通过当前的头布局的位置，得到下一个头布局的位置
//		int nextSectionPosition = getNextPosition(section + 1);
//		// 如果真实的items位置=下一个头布局的位置，就开始推动
//		if (nextSectionPosition != -1 && realPosition == nextSectionPosition - 1) {
//			return PINNED_HEADER_PUSHED_UP;
//		}
//
//		return PINNED_HEADER_VISIBLE;
//	}
//
//	@Override
//	public void configurePinnedHeader(View headerView, int position) {
//		// 得到当前的头布局的位置
//		int currentPosition = getCurrentPosition(position);
//		// 得到当前头布局的位置，通过位置集合得到在数据集合中的位置
//		int currentItemPosition = arrayListPosition.get(currentPosition);
//		// 显示出需要显示的数据
//		((TextView) headerView.findViewById(R.id.book_head_item)).setText(arrayList.get(currentItemPosition).toString());
//
//	}
//
//	// ---------------------------------------------------以上PinnedHeadListViewAdapter的回调-------------------------------------------------------------------------
//
//	/**
//	 * 通过真实的items位置，得到当前头布局的位置
//	 */
//	public int getCurrentPosition(int position) {
//		if (position < 0 || position >= getCount()) {
//			return -1;
//		}
//		int index = Arrays.binarySearch(arrayListPosition.toArray(), position);
//		return index >= 0 ? index : -index - 2;
//	}
//
//	/**
//	 * 通过当前的头布局的位置，得到下一个头布局的位置
//	 */
//	public int getNextPosition(int section) {
//		if (section < 0 || section >= arrayListPosition.size()) {
//			return -1;
//		}
//		// arrayListPosition.get(section)得到下一个元素，（arrayListPosition存放的也是位置）
//		return arrayListPosition.get(section);
//	}
//}
