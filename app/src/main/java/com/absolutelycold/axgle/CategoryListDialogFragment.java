package com.absolutelycold.axgle;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * <p>A fragment that shows a list of items as a modal bottom sheet.</p>
 * <p>You can show this modal bottom sheet from your activity like this:</p>
 * <pre>
 *     CategoryListDialogFragment.newInstance(30).show(getSupportFragmentManager(), "dialog");
 * </pre>
 * <p>You activity (or fragment) needs to implement {@link CategoryListDialogFragment.Listener}.</p>
 */
public class CategoryListDialogFragment extends BottomSheetDialogFragment {

    private static final String ARG_ITEM_COUNT = "item_count";
    public static final String TAG = "category_options";
    private Listener mListener;

    public static CategoryListDialogFragment newInstance(ArrayList<String> categoryArrayList) {
        final CategoryListDialogFragment fragment = new CategoryListDialogFragment();
        final Bundle args = new Bundle();
        args.putStringArrayList(ARG_ITEM_COUNT, categoryArrayList);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_category_list_dialog, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        final RecyclerView recyclerView = (RecyclerView) view;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new CategoryAdapter(getArguments().getStringArrayList(ARG_ITEM_COUNT)));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        final Fragment parent = getParentFragment();
        if (parent != null) {
            mListener = (Listener) parent;
        } else {
            mListener = (Listener) context;
        }
    }

    @Override
    public void onDetach() {
        mListener = null;
        super.onDetach();
    }

    public interface Listener {
        void onCategoryClicked(int position);
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        TextView text;

        ViewHolder(LayoutInflater inflater, ViewGroup parent, int resourceId) {
            super(inflater.inflate(resourceId, parent, false));

            if (getArguments().getStringArrayList(ARG_ITEM_COUNT) != null) {
                text = (TextView) itemView.findViewById(R.id.category_txt);
                text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mListener != null) {
                            mListener.onCategoryClicked(getAdapterPosition());
                            dismiss();
                        }
                    }
                });
            }
        }

    }

    public class LoadCircleViewHolader extends RecyclerView.ViewHolder {

        LinearLayout progressBar;
        public LoadCircleViewHolader(@NonNull LinearLayout itemView) {
            super(itemView);
            progressBar = itemView;
        }
    }
    private class CategoryAdapter extends RecyclerView.Adapter<ViewHolder> {

        private final ArrayList<String> categoryList;

        CategoryAdapter(ArrayList<String> itemCount) {
            categoryList = itemCount;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (categoryList == null) {
                return new ViewHolder(LayoutInflater.from(parent.getContext()), parent, R.layout.load_more_circle);
            }
            else {
                return new ViewHolder(LayoutInflater.from(parent.getContext()), parent, R.layout.fragment_category_list_dialog_item);
            }

        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            if (categoryList != null) {
                holder.text.setText(categoryList.get(position));
            }
        }

        @Override
        public int getItemCount() {
            if (categoryList == null) {
                return 1;
            }
            return categoryList.size();
        }

    }

}
