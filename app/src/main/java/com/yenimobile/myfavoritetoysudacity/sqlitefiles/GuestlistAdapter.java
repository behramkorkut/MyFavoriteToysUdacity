package com.yenimobile.myfavoritetoysudacity.sqlitefiles;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yenimobile.myfavoritetoysudacity.R;

public class GuestlistAdapter extends RecyclerView.Adapter<GuestlistAdapter.GuestViewHolder> {

    private Context mContext;
    private Cursor mCursor;


    public GuestlistAdapter(Context context, Cursor cursor){
        mContext = context;
        mCursor = cursor;
    }

    @NonNull
    @Override
    public GuestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.guestlist_item, parent, false);
        GuestViewHolder viewHolder = new GuestViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull GuestViewHolder holder, int position) {
        if(!mCursor.moveToPosition(position))
            return;

        String namestring = mCursor.getString(mCursor.getColumnIndex(WaitlistContract.WaitlistEntry.COLUMN_GUEST_NAME));
        int partySizeInt = mCursor.getInt(mCursor.getColumnIndex(WaitlistContract.WaitlistEntry.COLUMN_PARTY_SIZE));

        long id = mCursor.getLong(mCursor.getColumnIndex(WaitlistContract.WaitlistEntry._ID));

        holder.nameTV.setText(namestring);
        holder.partySizeTV.setText(String.valueOf(partySizeInt));
        holder.itemView.setTag(id);

        if (position %2 == 0){
            holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(),
                    R.color.material50Green));
        }else {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(),
                    R.color.material150Green));
        }
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    public void swpaCursor(Cursor newCursor){
        if (mCursor != null) mCursor.close();
        mCursor = newCursor;
        if (mCursor != null) notifyDataSetChanged();

    }
    //----------------------------------the view Holder -------------------------------------------
    public class GuestViewHolder extends RecyclerView.ViewHolder {

        private TextView nameTV, partySizeTV;

        public GuestViewHolder(View itemView) {
            super(itemView);
            nameTV = itemView.findViewById(R.id.guestlist_name_tv);
            partySizeTV = itemView.findViewById(R.id.guestlist_partysize_tv);
        }
    }
    //-----------------------------end of the view Holder -------------------------------------------


}
