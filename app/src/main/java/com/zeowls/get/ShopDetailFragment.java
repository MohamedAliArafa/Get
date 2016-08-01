package com.zeowls.get;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ShopDetailFragment extends Fragment {


    int id = 0;
    ImageView Shop_Pic, ShopHeader_Pic;
    LinearLayout Linear_Location;
    Picasso picasso;
    public ActionBar supportActionBar;
    static ArrayList<ItemDataMode> items = new ArrayList<>();
    RecyclerView recyclerView;
    MainAdapter adapter;
    LinearLayout Shop_Detail_Root_Layout;
    private ProgressBar mProgressBar;
    LinearLayout mErrorText;


    Button Shop_Items_btn;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    String shop_address_Value, shop_name_value, shop_description_value, shop_profile_pic_value, shop_cover_pic_value;
    TextView shop_address, Shop_Name;


    // private static final int NUM_PAGES = 5;

    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.9f;
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS = 0.3f;
    private static final int ALPHA_ANIMATIONS_DURATION = 1000;

    private boolean mIsTheTitleVisible = false;
    private boolean mIsTheTitleContainerVisible = true;

    private LinearLayout mTitleContainer;

    protected FragmentActivity myContext;

    loadingData2 loadingData;
    ShopAllItemsFragment shopAllItemsFragment;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Intent intent = getActivity().getIntent();
        id = intent.getIntExtra("Shop_Id", 0);

        Log.d("Shop Id in Shop Detail", String.valueOf(id));
        if (id !=0) {
            loadingData = new loadingData2();
            if (loadingData.getStatus() != AsyncTask.Status.RUNNING) {
                loadingData.execute();
            }
            new loadingData().execute();
        }else {
            getActivity().finish();
        }

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        supportActionBar = ((ShopDetailActivity) getActivity()).getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setHomeButtonEnabled(true);
        }
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_shop_detail, container, false);
    }

    @Override
    public void onAttach(Activity activity) {
        myContext = (FragmentActivity) activity;
        super.onAttach(activity);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = getArguments();
        String actionTitle = "";
        Bitmap imageBitmap = null;
        String transText = "";
        String transitionName = "";

        if (bundle != null) {
            transitionName = bundle.getString("TRANS_NAME");
            actionTitle = bundle.getString("ACTION");
            imageBitmap = bundle.getParcelable("IMAGE");
            transText = bundle.getString("TRANS_TEXT");
        }

        mProgressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        Shop_Detail_Root_Layout = (LinearLayout) view.findViewById(R.id.Shop_Detail_Root_Layout);
        Shop_Detail_Root_Layout.setVisibility(View.GONE);
        mErrorText = (LinearLayout) view.findViewById(R.id.error);

        Shop_Name = (TextView) view.findViewById(R.id.item_Detail_Shop_title1);
        Shop_Pic = (ImageView) view.findViewById(R.id.item_Detail_SHop_Image);
        ShopHeader_Pic = (ImageView) view.findViewById(R.id.image);
        Linear_Location = (LinearLayout) view.findViewById(R.id.Linear_Location);
        shop_address = (TextView) view.findViewById(R.id.shop_address);
        recyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        Shop_Items_btn = (Button) view.findViewById(R.id.Shop_Items_btn);

        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
        recyclerView.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
        recyclerView.setOnTouchListener(null);
        recyclerView.setHorizontalScrollBarEnabled(false);
        recyclerView.setVerticalScrollBarEnabled(false);
        recyclerView.setEnabled(false);
        recyclerView.setNestedScrollingEnabled(false);

        picasso = Picasso.with(getActivity());

        adapter = new MainAdapter();
        GridLayoutManager manager = new GridLayoutManager(getActivity(), getResources().getInteger(R.integer.grid_span));
        recyclerView.setLayoutManager(manager);
        adapter.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);


//        adapter = new ContentAdapter();
//
//        recyclerView.setAdapter(adapter);
//        recyclerView.setHasFixedSize(false);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        if (imageBitmap != null) {
            Shop_Pic.setImageBitmap(imageBitmap);
            ShopHeader_Pic.setImageBitmap(imageBitmap);
        }


        Shop_Items_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                fragmentManager = getFragmentManager();
//                fragmentTransaction = fragmentManager.beginTransaction();
//                shopAllItemsFragment =  new ShopAllItemsFragment();
//                shopAllItemsFragment.setId(id);
//                fragmentTransaction.add(R.id.fragment_main, shopAllItemsFragment);
//                fragmentTransaction.addToBackStack(null);
//                fragmentTransaction.commit();
            }
        });


    }


    private void updateUI() {

        Shop_Name.setText(shop_name_value);
        shop_address.setText(shop_address_Value);
        picasso.load(shop_profile_pic_value).fit().centerInside().into(Shop_Pic);
        picasso.load(shop_cover_pic_value).fit().into(ShopHeader_Pic);


    }

    public void setId(int id) {
        this.id = id;
    }


    private class loadingData2 extends AsyncTask<Void, Void, Object> {

        JSONObject itemsJSON;

        @Override
        protected void onPreExecute() {
            Context context = getContext();
            picasso = Picasso.with(context);
        }

        @Override
        protected Object doInBackground(Void... params) {
            Core core = new Core(getContext());
            try {
                itemsJSON = core.getShop(id);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            try {

                JSONObject item = itemsJSON;

                if (!item.getString("description").equals("null") && !item.getString("description").isEmpty()) {
                    shop_description_value = item.getString("description");
                }

                if (!item.getString("name").equals("null") && !item.getString("name").isEmpty()) {
                    shop_name_value = item.getString("name");
                }

                if (!item.getString("profile_pic").equals("null")) {
                    shop_profile_pic_value = "http://bubble.zeowls.com/uploads/" + item.getString("profile_pic");
                }

                if (!item.getString("cover_pic").equals("null")) {
                    shop_cover_pic_value = "http://bubble.zeowls.com/uploads/" + item.getString("cover_pic");
                }


                if (!item.getString("shop_address").equals("null") && !item.getString("shop_address").isEmpty()) {
                    shop_address_Value = item.getString("shop_address");
                } else {
                    Linear_Location.setVisibility(View.GONE);
                }


                updateUI();


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onPause() {
        if (loadingData.getStatus() == AsyncTask.Status.RUNNING) {
            loadingData.cancel(true);
        }
        super.onPause();
    }


    private class loadingData extends AsyncTask {

        @Override
        protected void onPreExecute() {
            items.clear();
        }

        @Override
        protected void onPostExecute(Object o) {
            mProgressBar.setVisibility(View.GONE);
            Shop_Detail_Root_Layout.setVisibility(View.VISIBLE);
            recyclerView.setAdapter(adapter);
        }

        @Override
        protected Object doInBackground(Object[] params) {

            try {
                Core core = new Core(getContext());
                JSONArray jsonArray = core.getShopItems(id);
                if (jsonArray != null && jsonArray.length() != 0) {
                    Log.d("json", jsonArray.toString());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject item = jsonArray.getJSONObject(i);
                        ItemDataMode item1 = new ItemDataMode();
                        item1.setCatId(item.getInt("cat_id"));
                        item1.setDesc(item.getString("description"));
                        item1.setId(item.getInt("id"));
                        item1.setName(item.getString("name"));
                        item1.setPrice(item.getString("price"));
                        item1.setShopId(item.getInt("shop_id"));
                        item1.setShortDesc(item.getString("short_description"));
                        item1.setImgUrl(item.getString("image"));

                        items.add(item1);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();

            }
            return null;
        }
    }


//    public class ViewHolder extends RecyclerView.ViewHolder {
//
//        public ViewHolder(LayoutInflater inflater, ViewGroup parent) {
//            super(inflater.inflate(R.layout.category_item_card, parent, false));
//
//
//            // Adding Snackbar to Action Button inside card
//            ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
//            TextView textView = (TextView) itemView.findViewById(R.id.name);
//            TextView price = (TextView) itemView.findViewById(R.id.price);
//
//
////            imageView.setOnClickListener(new View.OnClickListener() {
////                @Override
////                public void onClick(View v) {
//////                    Context context =  v.getContext();
//////                    context.startActivity(new Intent(context, ItemDetailActivity_2.class));
////                    Snackbar.make(v, "Image is pressed",
////                            Snackbar.LENGTH_LONG).show();
////                }
////            });
//
//
////            textView.setOnClickListener(new View.OnClickListان شاء الله ener() {
////                @Override
////                public void onClick(View v) {
////                    Snackbar.make(v, "Text is pressed",
////                            Snackbar.LENGTH_LONG).show();
////                }
////            });
//
//
////            price.setOnClickListener(new View.OnClickListener() {
////                @Override
////                public void onClick(View v) {
////                    Snackbar.make(v, "Price is pressed",
////                            Snackbar.LENGTH_LONG).show();
////                }
////            });
//
//        }
//    }

//    public class ContentAdapter extends RecyclerView.Adapter<ViewHolder> {
//        // Set numbers of Card in RecyclerView.
//        private static final int LENGTH = 18;
//        Item_Detail_Fragment fragment;
//        FragmentManager fragmentManager;
//        FragmentTransaction fragmentTransaction;
//
//        @Override
//        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
//        }
//
//        @Override
//        public void onBindViewHolder(ViewHolder holder, final int position) {
//            // no-op
//            if (items.size() != 0) {
//                ImageView image = (ImageView) holder.itemView.findViewById(R.id.imageView);
//                TextView name = (TextView) holder.itemView.findViewById(R.id.name);
//                TextView description = (TextView) holder.itemView.findViewById(R.id.description);
//                TextView price = (TextView) holder.itemView.findViewById(R.id.price);
//                name.setText(items.get(position).getName());
//                description.setText(items.get(position).getDesc());
//                price.setText(items.get(position).getPrice());
//                if (items.get(position).getImgUrl().equals("http://bubble.zeowls.com/uploads/")) {
//                    image.setImageResource(R.drawable.bubble_logo);
//                } else {
//                    Picasso.with(getContext()).load(items.get(position).getImgUrl()).into(image);
//                }
//                //text.setText(shops.get(position).getDescription());
//            }
//
//
//            holder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    fragmentManager = myContext.getSupportFragmentManager();
//                    fragmentTransaction = fragmentManager.beginTransaction();
////                    if (fragment != null) {
////                        fragmentTransaction.remove(fragment);
////                    }
//                    fragment = new Item_Detail_Fragment();
//                    fragment.setId(items.get(position).getId());
//                    fragmentTransaction.addToBackStack(null);
//                    fragmentTransaction.replace(R.id.fragment_main, fragment);
//                    fragmentTransaction.commit();
////                    Context context = v.getContext();
////                    Toast.makeText(context, "id: " + shops.get(position).getId(), Toast.LENGTH_SHORT).show();
////                    Intent intent = new Intent(context, ItemDetailActivity.class);
////                    intent.putExtra("id", shops.get(position).getId());
////                    context.startActivity(intent);
//                }
//            });
//        }
//
//        @Override
//        public int getItemCount() {
//            return items.size();
//        }
//    }


    public class MainAdapter extends SectionedRecyclerViewAdapter<MainAdapter.MainVH> {

        ItemDetailFragment endFragment2;

        @Override
        public int getSectionCount() {
            return 1;
        }

        @Override
        public int getItemCount(int section) {
            return items.size();
        }

        @Override
        public void onBindHeaderViewHolder(MainVH holder, int section) {
//            holder.ItemName.setText(String.format("Section %d", section));
            holder.ItemName.setText("");
        }

        @Override
        public void onBindViewHolder(final MainVH holder, int section, int relativePosition, final int absolutePosition) {
            holder.ItemName.setText(String.format("S:%d, P:%d, A:%d", section, relativePosition, absolutePosition));

            final String imageTransitionName = "transition" + absolutePosition;
            final String textTransitionName = "transtext" + absolutePosition;
            final Bundle bundle = new Bundle();


            if (items.size() != 0) {
                Log.d("Array size", String.valueOf(items.size()));
                holder.ItemName.setText(items.get(absolutePosition).getName());
                holder.ShopName.setText(items.get(absolutePosition).getShopName());
                holder.ItemPrice.setText("$" + String.valueOf(items.get(absolutePosition).getPrice()));
                if (items.get(absolutePosition).getImgUrl().equals("http://bubble.zeowls.com/uploads/")) {
                    holder.imageView.setImageResource(R.drawable.bubble_logo);
                } else {
                    picasso.load(items.get(absolutePosition).getImgUrl()).fit().centerCrop().into(holder.imageView);
                }
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    holder.cardView.setCardElevation(20);
//                    bundle.putString("TRANS_NAME", imageTransitionName);
//                    bundle.putString("TRANS_TEXT", textTransitionName);
//
//                    bundle.putString("ACTION", holder.ItemName.getText().toString());
//                    if (holder.imageView.getDrawable() != null) {
//                        bundle.putParcelable("IMAGE", ((BitmapDrawable) holder.imageView.getDrawable()).getBitmap());
//                    }
//
//                    endFragment2 = new ItemDetailFragment();
//                    endFragment2.setArguments(bundle);
//                    FragmentManager fragmentManager = getFragmentManager();
//                    endFragment2.setId(items.get(absolutePosition).getId());
//                    endFragment2.setShopId(items.get(absolutePosition).getShopId());
//                    fragmentManager.beginTransaction()
//                            .add(R.id.fragment_main, endFragment2)
//                            .addToBackStack(null)
//                            .addSharedElement(holder.imageView, imageTransitionName)
//                            .addSharedElement(holder.ItemName, textTransitionName)
//                            .commit();

                    Intent intent = new Intent(getActivity(), ItemDetailActivity.class);
                    intent.putExtra("id", items.get(absolutePosition).getId());
                    intent.putExtra("ShopId", id);
                    getActivity().startActivity(intent);

                }
            });

        }

        @Override
        public int getItemViewType(int section, int relativePosition, int absolutePosition) {
//        if (section == 1)
//            return 0; // VIEW_TYPE_HEADER is -2, VIEW_TYPE_ITEM is -1. You can return 0 or greater.
            return super.getItemViewType(section, relativePosition, absolutePosition);
        }

        @Override
        public MainVH onCreateViewHolder(ViewGroup parent, int viewType) {
            int layout;
            switch (viewType) {
                case VIEW_TYPE_HEADER:
                    layout = R.layout.list_item_header_3;
                    break;
                case VIEW_TYPE_ITEM:
                    layout = R.layout.list_item_main_3;
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
            ImageView imageView;

            public MainVH(View itemView) {
                super(itemView);

                ShopName = (TextView) itemView.findViewById(R.id.card_Shop_name);
                ItemName = (TextView) itemView.findViewById(R.id.card_Name);
                ItemPrice = (TextView) itemView.findViewById(R.id.share_button);
                cardView = (CardView) itemView.findViewById(R.id.card_view);
                imageView = (ImageView) itemView.findViewById(R.id.card_image);

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
    }


}
