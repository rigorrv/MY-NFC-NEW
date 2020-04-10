package com.jetruby.nfc.example;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class InfoContact extends BaseAdapter {
    Context context;
    public static List<Subject> subject_list;
    public InfoContact(List<Subject> listValue, Context context){
        this.context = context;
        this.subject_list=listValue;
    }
    @Override
    public int getCount (){
        if(this.subject_list==null){
            //error
        }else{
            return this.subject_list.size();
        }
        return 0;
    }
    @Override
    public Object getItem(int position){
        return this.subject_list.get(position);
    }
    @Override
    public long getItemId(int position){
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        ViewItemInfo viewItem = null;
        if(convertView == null){
            viewItem = new ViewItemInfo();
            LayoutInflater layoutInflater = (LayoutInflater)this.context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

            convertView = layoutInflater.inflate(R.layout.info_contact, null);

            viewItem.name=convertView.findViewById(R.id.name);
            viewItem.last=convertView.findViewById(R.id.last);
            viewItem.email=convertView.findViewById(R.id.email);
            viewItem.phone=convertView.findViewById(R.id.phone);
            viewItem.age=convertView.findViewById(R.id.age);
            viewItem.education=convertView.findViewById(R.id.education);
            viewItem.join=convertView.findViewById(R.id.join);
            viewItem.out=convertView.findViewById(R.id.out);
            viewItem.software=convertView.findViewById(R.id.software);
            viewItem.goals=convertView.findViewById(R.id.goals);
            viewItem.city=convertView.findViewById(R.id.city);
            convertView.setTag(viewItem);
        }else{
            viewItem = (ViewItemInfo) convertView.getTag();
        }
        viewItem.name.setText(subject_list.get(position).subject_name);
        viewItem.last.setText(subject_list.get(position).subject_last);
        viewItem.email.setText(subject_list.get(position).subject_email);
        viewItem.phone.setText(subject_list.get(position).subject_phone);
        viewItem.age.setText(subject_list.get(position).subject_age);
        viewItem.education.setText(subject_list.get(position).subject_education);
        viewItem.join.setText(subject_list.get(position).subject_join);
        viewItem.out.setText(subject_list.get(position).subject_out);
        viewItem.software.setText(subject_list.get(position).subject_software);
        viewItem.goals.setText(subject_list.get(position).subject_goals);
        viewItem.city.setText(subject_list.get(position).subject_city);
        return convertView;
    }
}

class ViewItemInfo{
    TextView name, last, email, phone, age, education, join, out, software, goals, city;

}
