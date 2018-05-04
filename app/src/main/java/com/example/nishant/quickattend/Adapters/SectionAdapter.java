package com.example.nishant.quickattend.Adapters;

import android.app.Activity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.nishant.quickattend.R;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jeremy on 4/29/18.
 */

public class SectionAdapter extends ArrayAdapter<JsonObject> {
    private final Activity context;
    private final List<JsonObject> sections;

    public SectionAdapter(Activity context, List<JsonObject> sections) {
        super(context, R.layout.section_adpter, sections);
        this.context = context;
        this.sections = sections;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.section_adpter, null, true);
        TextView className = rowView.findViewById(R.id.class_name);
        TextView classDays = rowView.findViewById(R.id.class_days);
        TextView classNbr = rowView.findViewById(R.id.class_number);

        JsonObject jObj = sections.get(position);

        List<String> days = new ArrayList<>();
        for (JsonElement dayElem: jObj.get("days").getAsJsonArray()) {
            JsonObject day = dayElem.getAsJsonObject();

            days.add(StringUtils.capitalize(day.get("day").getAsString().substring(0, 3)));
        }

        JsonObject course = jObj.get("course").getAsJsonObject();

        className.setText(course.get("code").getAsString() + " - " + course.get("name").getAsString());
        classNbr.setText(course.get("code").getAsString().split(" ")[1]);
        classDays.setText(TextUtils.join(" - ", days));

        return rowView;
    }
}
