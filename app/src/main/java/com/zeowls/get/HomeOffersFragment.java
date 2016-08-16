package com.zeowls.get;


import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zeowls.get.BackEnd.Core;
import com.zeowls.get.Models.ItemDataMode;
import com.zeowls.get.Widgets.SpacesItemDecoration;
import com.zeowls.get.adapters.SectionedRecyclerViewAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeOffersFragment extends Fragment {
    static ArrayList<ItemDataMode> GiftItems = new ArrayList<>();
    static ArrayList<ItemDataMode> CategoryList = new ArrayList<>();
    loadingData loadingData;
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private LinearLayout mErrorText;

    MainAdapter adapter;
    private Picasso picasso;


    public HomeOffersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        loadingData = new loadingData();
        if (loadingData.getStatus() != AsyncTask.Status.RUNNING) {
            loadingData.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }

        View v = inflater.inflate(R.layout.fragment_home_offers, container, false);

        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        mErrorText = (LinearLayout) view.findViewById(R.id.error);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
        picasso = Picasso.with(getActivity());
        adapter = new MainAdapter();
        GridLayoutManager manager = new GridLayoutManager(getActivity(), getResources().getInteger(R.integer.grid_span));
        mRecyclerView.setLayoutManager(manager);
        adapter.setLayoutManager(manager);
        mRecyclerView.setAdapter(adapter);
        super.onViewCreated(view, savedInstanceState);
    }


    private class loadingData extends AsyncTask {

        @Override
        protected void onPreExecute() {
            CategoryList.clear();
            GiftItems.clear();
        }

        @Override
        protected void onPostExecute(Object o) {
            if (GiftItems.size() != 0) {
                mRecyclerView.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.GONE);
                mRecyclerView.setAdapter(adapter);
            } else {
                mErrorText.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.GONE);
            }
            Log.d("Gifts Array", GiftItems.toString());
        }

        @Override
        protected Object doInBackground(Object[] params) {
            try {
                Core core = new Core(getActivity());
                JSONArray sectionsArray = core.getHomePage();
                for (int i = 0; i < sectionsArray.length(); i++) {
                    String sectionName = sectionsArray.getJSONObject(i).getString("Catname");
                    JSONArray sectionItems = sectionsArray.getJSONObject(i).getJSONArray("Items");
                    int itemsCount = sectionItems.length();
                    if (itemsCount > 1) {
                        ItemDataMode category = new ItemDataMode();
                        category.setName(sectionName);
                        category.setCatId(itemsCount);
                        CategoryList.add(category);
                        for (int y = 0; y < sectionItems.length(); y++) {
                            ItemDataMode Gift_Item = new ItemDataMode();
                            JSONObject item = sectionItems.getJSONObject(y);
                            Gift_Item.setId(item.getInt("id"));
                            Gift_Item.setName(item.getString("name"));
                            Gift_Item.setShopName(item.getString("shop_name"));
                            Gift_Item.setShopId(item.getInt("shop_id"));
                            Gift_Item.setDesc(item.getString("description"));
                            Gift_Item.setPrice("$" + item.getString("price"));
                            Gift_Item.setImgUrl(item.getString("image"));
                            GiftItems.add(Gift_Item);
                        }
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    // main Adapter

    public class MainAdapter extends SectionedRecyclerViewAdapter<MainAdapter.MainVH> {

        final ItemDetailFragment endFragment2 = new ItemDetailFragment();

        @Override
        public int getSectionCount() {
//            if (GiftItems.size() != 0) {
//                return GiftItems.size() / 4;
//            }
            if (CategoryList.size() != 0) {
                return CategoryList.size();
            }
            return 0;

        }

        @Override
        public int getItemCount(int section) {
//        if (section % 2 == 0)
//            return 2; // even sections get 4 items
            return CategoryList.get(section).getCatId(); // odd get 8
        }

        @Override
        public void onBindHeaderViewHolder(MainVH holder, int section) {
//            holder.ItemName.setText(String.format("Section %d", section));
            holder.ItemName.setText(CategoryList.get(section).getName());
        }

        @Override
        public void onBindViewHolder(final MainVH holder, int section, int relativePosition, final int absolutePosition) {
            holder.ItemName.setText(String.format("S:%d, P:%d, A:%d", section, relativePosition, absolutePosition));

            final String imageTransitionName = "transition" + absolutePosition;
            final String textTransitionName = "transtext" + absolutePosition;
            final Bundle bundle = new Bundle();


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                holder.ItemName.setTransitionName(textTransitionName);
                holder.imageView.setTransitionName(imageTransitionName);
                setExitTransition(TransitionInflater.from(getActivity()).inflateTransition(android.R.transition.fade));
                endFragment2.setSharedElementEnterTransition(TransitionInflater.from(getActivity()).inflateTransition(R.transition.change_image_trans));
                endFragment2.setSharedElementReturnTransition(TransitionInflater.from(getActivity()).inflateTransition(R.transition.change_image_trans));
                endFragment2.setEnterTransition(TransitionInflater.from(getActivity()).inflateTransition(android.R.transition.fade));
            }

            if (GiftItems.size() != 0) {
                Log.d("Array size", String.valueOf(GiftItems.size()));
                Log.d("Array absolutePosition", String.valueOf(absolutePosition));
                holder.ItemName.setText(GiftItems.get(absolutePosition).getName());
                holder.ShopName.setText(GiftItems.get(absolutePosition).getShopName());
                holder.ItemPrice.setText(String.valueOf(GiftItems.get(absolutePosition).getPrice()));
                if (GiftItems.get(absolutePosition).getImgUrl().equals("http://bubble.zeowls.com/uploads/")) {
                    holder.imageView.setImageResource(R.drawable.android);
                } else {
                    picasso.load(GiftItems.get(absolutePosition).getImgUrl()).fit().centerCrop().into(holder.imageView);
                }
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.cardView.setCardElevation(20);
                    bundle.putString("TRANS_NAME", imageTransitionName);
                    bundle.putString("TRANS_TEXT", textTransitionName);

                    bundle.putString("ACTION", holder.ItemName.getText().toString());
                    if (holder.imageView.getDrawable() != null) {
                        bundle.putParcelable("IMAGE", ((BitmapDrawable) holder.imageView.getDrawable()).getBitmap());
                    }


                    Intent intent = new Intent(getActivity(), ItemDetailActivity.class);
                    intent.putExtra("id", GiftItems.get(absolutePosition).getId());
                    intent.putExtra("ShopId", GiftItems.get(absolutePosition).getShopId());
                    getActivity().startActivity(intent);
                }
            });

            holder.favBTN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Core core = new Core(getActivity());
                    // core.updateItemFavoriteDB(1, GiftItems.get(absolutePosition).getId());
                }
            });

        }

        @Override
        public int getItemViewType(int section, int relativePosition, int absolutePosition) {

            return super.getItemViewType(section, relativePosition, absolutePosition);
        }

        @Override
        public MainVH onCreateViewHolder(ViewGroup parent, int viewType) {
            int layout;
            switch (viewType) {
                case VIEW_TYPE_HEADER:
                    layout = R.layout.list_item_header;
                    break;
                case VIEW_TYPE_ITEM:
                    layout = R.layout.list_item_main;
                    break;
                default:
                    layout = R.layout.list_item_main_bold;
                    break;
            }

            View v = LayoutInflater.from(getActivity()).inflate(layout, parent, false);

            return new MainVH(v);
        }

        public class MainVH extends RecyclerView.ViewHolder {


            TextView ShopName;
            TextView ItemName;
            TextView ItemPrice;
            CardView cardView;
            ImageView imageView, favBTN;

            public MainVH(View itemView) {
                super(itemView);

                ShopName = (TextView) itemView.findViewById(R.id.card_Shop_name);
                ItemName = (TextView) itemView.findViewById(R.id.card_Name);
                ItemPrice = (TextView) itemView.findViewById(R.id.share_button);
                cardView = (CardView) itemView.findViewById(R.id.card_view);
                imageView = (ImageView) itemView.findViewById(R.id.card_image);
                favBTN = (ImageView) itemView.findViewById(R.id.card_Favorite);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Intent intent = new Intent(context, ItemDetailActivity_2.class);
//                        context.startActivity(intent);
                    }
                });


                ShopName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Toast.makeText(context, ShopName.getText().toString(), Toast.LENGTH_SHORT).show();
                    }
                });


                ItemName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Toast.makeText(context, ItemName.getText(), Toast.LENGTH_SHORT).show();
                    }
                });


                ItemPrice.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //   Toast.makeText(context, "item price", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    } // Main Adapter


    @Override
    public void onPause() {
        if (loadingData != null) {
            if (loadingData.getStatus() == AsyncTask.Status.RUNNING) {
                loadingData.cancel(true);
            }
        }
        super.onPause();
    }


}
