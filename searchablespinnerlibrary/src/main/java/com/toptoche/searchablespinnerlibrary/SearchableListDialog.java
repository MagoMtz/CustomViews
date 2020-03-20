package com.toptoche.searchablespinnerlibrary;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.SearchView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SearchableListDialog extends DialogFragment implements
        SearchView.OnQueryTextListener, SearchView.OnCloseListener {

    private static final String ITEMS = "items";
    private static final String IS_MULTI_SELECT = "is_multi_select";

    private ArrayAdapter listAdapter;

    private ListView _listViewItems;

    private SearchableItem _searchableItem;

    private OnSearchTextChanged _onSearchTextChanged;

    private SearchView _searchView;

    private String _strTitle;

    private String _strPositiveButtonText;

    private DialogInterface.OnClickListener _onClickListener;

    private MultipleItem _multipleItem;

    private boolean isMultiSelect;

    private ArrayList<Object> _selectedItems;

    public SearchableListDialog() {

    }

    public static SearchableListDialog newInstance(List items, boolean isMultiSelect) {
        SearchableListDialog multiSelectExpandableFragment = new
                SearchableListDialog();

        Bundle args = new Bundle();
        args.putSerializable(ITEMS, (Serializable) items);
        args.putBoolean(IS_MULTI_SELECT, isMultiSelect);

        multiSelectExpandableFragment.setArguments(args);

        return multiSelectExpandableFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams
                .SOFT_INPUT_STATE_HIDDEN);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Getting the layout inflater to inflate the view in an alert dialog.
        LayoutInflater inflater = LayoutInflater.from(getActivity());

        // Crash on orientation change #7
        // Change Start
        // Description: As the instance was re initializing to null on rotating the device,
        // getting the instance from the saved instance
        if (null != savedInstanceState) {
            _searchableItem = (SearchableItem) savedInstanceState.getSerializable("item");
            _multipleItem = (MultipleItem) savedInstanceState.getSerializable("multiple_item");
        }
        // Change End

        View rootView = inflater.inflate(R.layout.searchable_list_dialog, null);
        setData(rootView);

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setView(rootView);

        String strPositiveButton = _strPositiveButtonText == null ? "CLOSE" : _strPositiveButtonText;
        alertDialog.setPositiveButton(strPositiveButton, _onClickListener);

        String strTitle = _strTitle == null ? "Select Item" : _strTitle;
        alertDialog.setTitle(strTitle);

        final AlertDialog dialog = alertDialog.create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams
                .SOFT_INPUT_STATE_HIDDEN);
        return dialog;
    }

    // Crash on orientation change #7
    // Change Start
    // Description: Saving the instance of searchable item instance.
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("item", _searchableItem);
        outState.putSerializable("multiple_item", _multipleItem);
        super.onSaveInstanceState(outState);
    }
    // Change End

    public void setTitle(String strTitle) {
        _strTitle = strTitle;
    }

    public void setPositiveButton(String strPositiveButtonText) {
        _strPositiveButtonText = strPositiveButtonText;
    }

    public void setPositiveButton(String strPositiveButtonText, DialogInterface.OnClickListener onClickListener) {
        _strPositiveButtonText = strPositiveButtonText;
        _onClickListener = onClickListener;
    }

    public void setOnSearchableItemClickListener(SearchableItem searchableItem) {
        this._searchableItem = searchableItem;
    }

    public void setOnMultipleItemClickListener(MultipleItem multipleItem) {
        this._multipleItem = multipleItem;
    }

    public void setOnSearchTextChangedListener(OnSearchTextChanged onSearchTextChanged) {
        this._onSearchTextChanged = onSearchTextChanged;
    }

    protected ListView getListView() {
        return _listViewItems;
    }

    private void setData(View rootView) {
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context
                .SEARCH_SERVICE);

        _searchView = (SearchView) rootView.findViewById(R.id.search);
        _searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName
                ()));
        _searchView.setIconifiedByDefault(false);
        _searchView.setOnQueryTextListener(this);
        _searchView.setOnCloseListener(this);
        _searchView.clearFocus();
        InputMethodManager mgr = (InputMethodManager) getActivity().getSystemService(Context
                .INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(_searchView.getWindowToken(), 0);


        List items = (List) getArguments().getSerializable(ITEMS);
        // Added
        isMultiSelect = getArguments().getBoolean(IS_MULTI_SELECT);

        _listViewItems = (ListView) rootView.findViewById(R.id.listItems);

        // Added
        _selectedItems = new ArrayList<Object>();
        int view;
        if (isMultiSelect)
            view = android.R.layout.simple_list_item_multiple_choice;
        else
            view = android.R.layout.simple_list_item_1;
        //create the adapter by passing your ArrayList data
        // Added
        listAdapter = new SearchableListAdapter(getActivity(), items, isMultiSelect);
        //listAdapter = new ArrayAdapter(getActivity(), view,
        //        items);
        //attach the adapter to the list
        _listViewItems.setAdapter(listAdapter);

        _listViewItems.setTextFilterEnabled(true);

        _listViewItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (isMultiSelect) {
                    CheckedTextView checkedTextView;
                    if (view instanceof CheckedTextView) {
                        checkedTextView = ((CheckedTextView) view);
                    } else {
                        _multipleItem.onSelectionCanceled();
                        getDialog().dismiss();
                        return;
                    }

                    checkedTextView.setChecked(!checkedTextView.isChecked());

                    if (checkedTextView.isChecked()) {
                        _selectedItems.add(listAdapter.getItem(position).toString());
                        _multipleItem.onItemSelected(listAdapter.getItem(position));
                    } else {
                        _selectedItems.remove(listAdapter.getItem(position));
                        _multipleItem.onItemUnselected(listAdapter.getItem(position));
                    }

                    if (_selectedItems.size() == 2) {
                        String items = _selectedItems.get(0).toString() + ", " + _selectedItems.get(1).toString();
                        _multipleItem.onItemsSelected(items, _selectedItems);
                        getDialog().dismiss();
                    }
                } else {
                    _searchableItem.onSearchableItemClicked(listAdapter.getItem(position), position);
                    getDialog().dismiss();
                }
            }
        });
    }

    @Override
    public boolean onClose() {
        if (isMultiSelect) {
            _multipleItem.onSelectionCanceled();
        }
        return false;
    }

    @Override
    public void onPause()
    {
        super.onPause();
        dismiss();
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        _searchView.clearFocus();
        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
//        listAdapter.filterData(s);
        if (TextUtils.isEmpty(s)) {
//                _listViewItems.clearTextFilter();
            ((SearchableListAdapter) _listViewItems.getAdapter()).getFilter().filter(null);
        } else {
            ((SearchableListAdapter) _listViewItems.getAdapter()).getFilter().filter(s);
        }
        if (null != _onSearchTextChanged) {
            _onSearchTextChanged.onSearchTextChanged(s);
        }
        return true;
    }

    public interface SearchableItem<T> extends Serializable {
        void onSearchableItemClicked(T item, int position);
    }

    public interface OnSearchTextChanged {
        void onSearchTextChanged(String strText);
    }

    public interface MultipleItem extends Serializable {
        void onItemSelected(Object o);
        void onItemUnselected(Object o);
        void onItemsSelected(String items, List<Object> selectedItems);
        void onSelectionCanceled();
    }
}
