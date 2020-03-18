package com.toptoche.searchablespinnerlibrary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by jmartinez
 * @since 18/03/2020.
 */
public class SearchableListAdapter extends ArrayAdapter<Object> {
    private Context context;
    private List<Object> elements;
    private List<Object> originalElements;
    private boolean isMultiSelect;

    public SearchableListAdapter(Context context, List<Object> elements, boolean isMultiSelect) {
        super(context, 0, elements);
        this.context = context;
        this.elements = elements;
        this.isMultiSelect = isMultiSelect;
        this.originalElements = elements;
    }

    @Override
    public int getCount() {
        return elements.size();
    }

    @Override
    public Object getItem(int position) {
        return elements.get(position);
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (isMultiSelect && position > 0) {
            view = selectableView(position, parent);
        } else {
            if (isMultiSelect && !elements.get(0).equals(originalElements.get(0)))//elements.size() != originalElements.size())
                view = selectableView(position, parent);
            else
                view = noSelectableView(position, parent);
        }
        return view;
    }

    private View selectableView(int position, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_multiple_choice, parent, false);
        final CheckedTextView checkedTextView = view.findViewById(android.R.id.text1);
        Object item = getItem(position);

        checkedTextView.setText(item.toString());

        checkedTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkedTextView.setChecked(!checkedTextView.isChecked());
            }
        });
        return view;
    }

    private View noSelectableView(int position, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false);
        TextView textView = view.findViewById(android.R.id.text1);
        Object item = getItem(position);

        textView.setText(item.toString());

        return view;
    }

    private Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint == null)
                return null;

            FilterResults filterResults = new FilterResults();
            ArrayList tempList = new ArrayList<Object>();

            if (elements != null) {
                int i = 0;
                while (i < elements.size()) {
                    Object item = elements.get(i);
                    if (item.toString().toUpperCase().contains(constraint.toString().toUpperCase()))
                        tempList.add(item);
                    i++;
                }

                filterResults.values = tempList;
                filterResults.count = tempList.size();
            }

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results == null) {
                elements = originalElements;
                notifyDataSetChanged();
                return;
            }

            if (results.count > 0) {
                elements = (List<Object>) results.values;
                notifyDataSetChanged();
            } else {
                elements = new ArrayList<>();
                notifyDataSetInvalidated();
            }
        }
    };

}
