package com.desenvolvigames.applications.witalk.control;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.desenvolvigames.applications.witalk.R;
import com.desenvolvigames.applications.witalk.entities.Contact;
import com.desenvolvigames.applications.witalk.utilities.AsyncImageView;

import java.util.List;

/**
 * Created by NOTEBOOK on 23/08/2017.
 */

public class ContactLayoutAdapter extends ArrayAdapter<Contact> {

    public ContactLayoutAdapter(@NonNull Context context, List<Contact> contatos) {
        super(context, 0, contatos);
    }

    private class ContatoHolder {
        public TextView userNick;
        public TextView userStatus;
        public ImageView userImageResource;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        ContatoHolder holder = new ContatoHolder();

        // First let's verify the convertView is not null
        if (convertView == null) {
            // This a new view we inflate the new layout
            LayoutInflater inflater = LayoutInflater.from(getContext());
            v = inflater.inflate(R.layout.activity_lobby_adapter, parent, false);
            // Now we can fill the layout with the right values
            holder.userNick = (TextView) v.findViewById(R.id.userNick);
            holder.userStatus = (TextView) v.findViewById(R.id.userStatus);
            holder.userImageResource = (ImageView) v.findViewById(R.id.userImageResource);
            v.setTag(holder);
        }
        else
            holder = (ContatoHolder) v.getTag();

        Contact p = getItem(position);
        holder.userNick.setText(p.mNome);
        holder.userStatus.setText(p.mStatus);
//            Picasso.with(getContext()).load(p.ImagemContato).into(holder.imgContato);
//            Picasso.with(getContext()).load("http://i.imgur.com/DvpvklR.png").into(holder.imgContato);

        AsyncImageView asyncContatoAdapter = new AsyncImageView(holder.userImageResource);
        asyncContatoAdapter.execute(p.mUserImageResource);

        return v;
    }
}