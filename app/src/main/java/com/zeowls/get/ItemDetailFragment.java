package com.zeowls.get;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.zeowls.get.BackEnd.Core;
import com.zeowls.get.BackEnd.FireOwl;
import com.zeowls.get.Models.ItemDataMode;
import com.zeowls.get.Widgets.SpacesItemDecoration;
import com.zeowls.get.adapters.SectionedRecyclerViewAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class ItemDetailFragment extends Fragment {


    int item_id = 0;
    int shop_id = 0;
    int user_id = 0;
    ValueAnimator mAnimator;
    ValueAnimator mAnimator2;
    ValueAnimator mAnimator3;
    private ProgressBar mProgressBar;

    static ArrayList<ItemDataMode> items = new ArrayList<>();

    private ArrayList<String> ImagesArray = new ArrayList<>();

    MainAdapter adapter;


    TextView name, description, price, itemNameToolbar,
            shopName, item_detail_desc_2, item_detail_shop_name_2, item_detail_shop_Address, item_Detail_Name, item_detail_shop_Short_Desc, item_Qty;
    Button visitShop, addToCart;
    ImageView itemPic, item_Shop_Photo, item_Shop_Photo_2;
    RelativeLayout Details_Header, OverView_Header, Reviews_Header;
    LinearLayout Expandable_Reviews, Expandable_OverView, Expandable_Details;
    ImageView Reviews_Arrow_Down, Reviews_Arrow_Up,
            OverView_Arrow_Down, OverView_Arrow_Up,
            Details_Arrow_Down, Details_Arrow_Up;


    RecyclerView item_detail_Shop_Top_Items;
    LinearLayout Item_Detail_Root_Layout;
    ScrollView Fragment_Item_Detail_ScrollView;

    String item_name, item_price, item_image, item_desc, shop_name_txt, Shop_image, Shop_Address, Shop_Short_Desc, item_Qty_String;

    ShopDetailFragment endFragment = new ShopDetailFragment();
    LinearLayout mErrorText;


    Picasso picasso;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        picasso = Picasso.with(getActivity());
        Intent intent = getActivity().getIntent();
        item_id = intent.getIntExtra("id", 0);
        shop_id = intent.getIntExtra("ShopId", 0);
        if (item_id != 0) {
            new loadingData().execute();
            new loadingData2().execute();
        }else {
            getActivity().finish();
        }
        Log.d("Item Id", String.valueOf(item_id));
        Log.d("Shop Id", String.valueOf(shop_id));


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_item_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        Item_Detail_Root_Layout = (LinearLayout) view.findViewById(R.id.Item_Detail_Root_Layout);
        Item_Detail_Root_Layout.setVisibility(View.GONE);

        Fragment_Item_Detail_ScrollView = (ScrollView) view.findViewById(R.id.Fragment_Item_Detail_ScrollView);
        mErrorText = (LinearLayout) view.findViewById(R.id.error);


        description = (TextView) view.findViewById(R.id.item_detail_desc);
        item_detail_desc_2 = (TextView) view.findViewById(R.id.item_detail_desc_2);
        addToCart = (Button) view.findViewById(R.id.addToCart);
        price = (TextView) view.findViewById(R.id.item_detail_price);
        shopName = (TextView) view.findViewById(R.id.item_detail_shop_name);
        item_detail_shop_name_2 = (TextView) view.findViewById(R.id.item_detail_shop_name_2);
        item_detail_shop_Address = (TextView) view.findViewById(R.id.item_detail_shop_Address);
        item_Detail_Name = (TextView) view.findViewById(R.id.item_Detail_Name);
        item_detail_shop_Short_Desc = (TextView) view.findViewById(R.id.item_detail_shop_Short_Desc);
        item_Qty = (TextView) view.findViewById(R.id.item_Qty);
        item_detail_Shop_Top_Items = (RecyclerView) view.findViewById(R.id.item_detail_Shop_Top_Items);

        visitShop = (Button) view.findViewById(R.id.item_detail_shop_visit);
        //addToCart = (Button) view.findViewById(R.id.item_detail_addtocart);
        itemPic = (ImageView) view.findViewById(R.id.item_image_pager);

        Reviews_Header = (RelativeLayout) view.findViewById(R.id.Reviews_Header);
        OverView_Header = (RelativeLayout) view.findViewById(R.id.OverView_Header);
        Details_Header = (RelativeLayout) view.findViewById(R.id.Details_Header);

        Expandable_Reviews = (LinearLayout) view.findViewById(R.id.Expandable_Reviews);
        Expandable_OverView = (LinearLayout) view.findViewById(R.id.Expandable_OverView);
        Expandable_Details = (LinearLayout) view.findViewById(R.id.Expandable_Details);

        item_Shop_Photo_2 = (ImageView) view.findViewById(R.id.item_Shop_Photo_2);
        item_Shop_Photo = (ImageView) view.findViewById(R.id.item_Shop_Photo);

        Reviews_Arrow_Down = (ImageView) view.findViewById(R.id.Reviews_Arrow_Down);
        Reviews_Arrow_Up = (ImageView) view.findViewById(R.id.Reviews_Arrow_Up);

        Details_Arrow_Down = (ImageView) view.findViewById(R.id.Details_Arrow_Down);
        Details_Arrow_Up = (ImageView) view.findViewById(R.id.Details_Arrow_Up);

        OverView_Arrow_Down = (ImageView) view.findViewById(R.id.OverView_Arrow_Down);
        OverView_Arrow_Up = (ImageView) view.findViewById(R.id.OverView_Arrow_Up);

        //Set Arrow Up And Down
        OverView_Arrow_Up.setVisibility(View.GONE);
        Details_Arrow_Up.setVisibility(View.GONE);
        Reviews_Arrow_Up.setVisibility(View.GONE);


        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
        item_detail_Shop_Top_Items.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
        item_detail_Shop_Top_Items.setOnTouchListener(null);
        item_detail_Shop_Top_Items.setHorizontalScrollBarEnabled(false);
        item_detail_Shop_Top_Items.setVerticalScrollBarEnabled(false);
        item_detail_Shop_Top_Items.setEnabled(false);
        item_detail_Shop_Top_Items.setNestedScrollingEnabled(false);

        picasso = Picasso.with(getActivity());

        adapter = new MainAdapter();
        GridLayoutManager manager = new GridLayoutManager(getActivity(), getResources().getInteger(R.integer.grid_span));
        item_detail_Shop_Top_Items.setLayoutManager(manager);
        adapter.setLayoutManager(manager);
        item_detail_Shop_Top_Items.setAdapter(adapter);


        Bundle bundle = getArguments();
        String Title;
        Bitmap imageBitmap;
        String transText;
        String transitionName;

        if (bundle != null) {
            transitionName = bundle.getString("TRANS_NAME");
            Title = bundle.getString("ACTION");
            imageBitmap = bundle.getParcelable("IMAGE");
            transText = bundle.getString("TRANS_TEXT");
        }





    }


    public class MainAdapter extends SectionedRecyclerViewAdapter<MainAdapter.MainVH> {

        ItemDetailFragment endFragment2;

        @Override
        public int getSectionCount() {
            return 1;
        }

        @Override
        public int getItemCount(int section) {
            if (items.size() >= 5) {
                return 4;
            } else {
                return items.size();

            }
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
                    holder.imageView.setImageResource(R.drawable.android);
                } else {
                    picasso.load(items.get(absolutePosition).getImgUrl()).fit().centerCrop().into(holder.imageView);
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

//                    endFragment2 = new ItemDetailFragment();
//                    endFragment2.setArguments(bundle);
//                    FragmentManager fragmentManager = getFragmentManager();
//                    endFragment2.setId(items.get(absolutePosition).getId());
//                    endFragment2.setShopId(shop_id);
//                    fragmentManager.beginTransaction()
//                            .add(R.id.fragment_main, endFragment2)
//                            .addToBackStack(null)
//                            .addSharedElement(holder.imageView, imageTransitionName)
//                            .addSharedElement(holder.ItemName, textTransitionName)
//                            .commit();
                    Intent intent = new Intent(getActivity(), ItemDetailActivity.class);
                    intent.putExtra("id", items.get(absolutePosition).getId());
                    intent.putExtra("ShopId", items.get(absolutePosition).getShopId());

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


    public void setId(int id) {
        this.item_id = id;
    }

    public void setShopId(int id) {
        this.shop_id = id;
    }


    private class loadingData extends AsyncTask {

        JSONObject itemsJSON;

        @Override
        protected void onPostExecute(Object o) {
            try {


                mProgressBar.setVisibility(View.GONE);
                Item_Detail_Root_Layout.setVisibility(View.VISIBLE);


                JSONObject item = itemsJSON;

                item_name = item.getString("name");
                item_price = "$" + item.getString("price");
                item_image = item.getString("image");
                item_desc = item.getString("description");
                shop_name_txt = item.getString("shop_name");
                Shop_image = item.getString("shop_image");
                Shop_Address = item.getString("shop_address");
                Shop_Short_Desc = item.getString("shop_short_desc");
                item_Qty_String = String.valueOf(item.getInt("quantity"));
                shop_id = item.getInt("shop_id");

                updateUI();


                visitShop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

//                        String fragmentTag = "ShopFragment";
//                        String backStateName = this.getClass().getName();
//                        FragmentManager manager = getFragmentManager();
//                        if (manager.findFragmentByTag(fragmentTag) == null) { //fragment not in back stack, create it.
//                            FragmentTransaction ft = manager.beginTransaction();
//                            endFragment.setId(shop_id);
//                            if (getFragmentManager().entfindFragmentByTag("ItemDetailFragment") != null) {
//                                ft.hide(getFragmentManager().findFragmentByTag("ItemDetailFragment"));
//                            }
//                            ft.add(R.id.fragment_main, endFragment, fragmentTag);
//                            ft.addToBackStack(backStateName);
//                            ft.commit();
//                        } else {
//                            FragmentTransaction ft = manager.beginTransaction();
//                            ft.hide(getFragmentManager().findFragmentByTag("ItemDetailFragment"));
//                            ft.show(manager.findFragmentByTag(fragmentTag));
//                            ft.replace(R.id.fragment_main, manager.findFragmentByTag(fragmentTag));
//                            ft.addToBackStack(null);
//                            ft.commit();
//                        }



                        Intent intent = new Intent(getActivity(), ShopDetailActivity.class);
                        intent.putExtra("Shop_Id",shop_id);
                        getActivity().startActivity(intent);


                    }
                });


//                addToCart.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
////                        if (user_id != 0) {
//                        if (item_id != 0 && shop_id != 0) {
//                            new Core(getActivity()).addToCart(shop_id, item_id, item_name, item_price, item_image, item_desc, shop_name_txt);
//                            FireOwl fireOwl = new FireOwl(getActivity());
//                            fireOwl.addOrder(shop_id, item_id, user_id);
//
//                            Toast.makeText(getActivity(), item_name + " Added to cart", Toast.LENGTH_SHORT).show();
//
////                            ShoppingCartActivity endFragment2 = new ShoppingCartActivity();
////                            FragmentManager fragmentManager = getFragmentManager();
////                            fragmentManager.beginTransaction()
////                                    .hide(getFragmentManager().findFragmentByTag("homeFragment"))
////                                    .add(R.id.fragment_main, endFragment2)
////                                    .addToBackStack(null)
////                                    .commit();
////                                Intent intent = new Intent(getActivity(), ShoppingCartActivity.class);
////                                startActivity(intent);
//                        } else {
//                            Log.d("Id Empty", "Item And Shop Ids are Empty");
//                        }
////                        } else {
////                            Toast.makeText(getActivity(), "please login first", Toast.LENGTH_SHORT).show();
//////                            DialogFragment newFragment = new LoginFragment();
//////                            newFragment.show(getFragmentManager(), "missiles");
////                        }
//                    }
//                });


            } catch (JSONException e) {
                e.printStackTrace();
                mErrorText.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.GONE);
            }
        }

        @Override
        protected Object doInBackground(Object[] params) {
            Core core = new Core(getContext());
            try {
                itemsJSON = core.getItem(item_id);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }


    private void expand() {
        //set Visible
        Expandable_Reviews.setVisibility(View.VISIBLE);
        /* Remove and used in preDrawListener
        final int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		final int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		mLinearLayout.measure(widthSpec, heightSpec);

		mAnimator = slideAnimator(0, mLinearLayout.getMeasuredHeight());
		*/
        mAnimator.start();
    }


    private void collapse() {
        int finalHeight = Expandable_Reviews.getHeight();
        ValueAnimator mAnimator = slideAnimator(finalHeight, 0);
        mAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animator) {
                //Height=0, but it set visibility to GONE
                Expandable_Reviews.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });
        mAnimator.start();
    }

    private ValueAnimator slideAnimator(int start, int end) {
        ValueAnimator animator = ValueAnimator.ofInt(start, end);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                //Update Height
                int value = (Integer) valueAnimator.getAnimatedValue();

                ViewGroup.LayoutParams layoutParams = Expandable_Reviews.getLayoutParams();
                layoutParams.height = value;
                Expandable_Reviews.setLayoutParams(layoutParams);
            }
        });
        return animator;
    }


    private void expand2() {
        //set Visible
        Expandable_OverView.setVisibility(View.VISIBLE);
        /* Remove and used in preDrawListener
        final int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		final int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		mLinearLayout.measure(widthSpec, heightSpec);

		mAnimator = slideAnimator(0, mLinearLayout.getMeasuredHeight());
		*/
        mAnimator2.start();
    }

    private void collapse2() {

        int finalHeight = Expandable_OverView.getHeight();
        ValueAnimator mAnimator = slideAnimator2(finalHeight, 0);
        mAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animator) {
                //Height=0, but it set visibility to GONE
                Expandable_OverView.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });
        mAnimator.start();
    }

    private ValueAnimator slideAnimator2(int start, int end) {
        ValueAnimator animator = ValueAnimator.ofInt(start, end);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                //Update Height
                int value = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = Expandable_OverView.getLayoutParams();
                layoutParams.height = value;
                Expandable_OverView.setLayoutParams(layoutParams);
            }
        });
        return animator;
    }


    private void expand3() {
        //set Visible
        Expandable_Details.setVisibility(View.VISIBLE);
        /* Remove and used in preDrawListener
        final int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		final int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		mLinearLayout.measure(widthSpec, heightSpec);

		mAnimator = slideAnimator(0, mLinearLayout.getMeasuredHeight());
		*/
        mAnimator3.start();
    }

    private void collapse3() {
        int finalHeight = Expandable_Details.getHeight();
        ValueAnimator mAnimator = slideAnimator3(finalHeight, 0);
        mAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animator) {
                //Height=0, but it set visibility to GONE
                Expandable_Details.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });
        mAnimator.start();


    }


    private ValueAnimator slideAnimator3(int start, int end) {
        ValueAnimator animator = ValueAnimator.ofInt(start, end);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                //Update Height
                int value = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = Expandable_Details.getLayoutParams();
                layoutParams.height = value;
                Expandable_Details.setLayoutParams(layoutParams);
            }
        });
        return animator;
    }


    @Override
    public void onResume() {
        SharedPreferences prefs = getActivity().getSharedPreferences("Credentials", getActivity().MODE_PRIVATE);
        user_id = prefs.getInt("id", 0);
        super.onResume();
    }


//    public class CustomDialogFragment extends DialogFragment {
//        /**
//         * The system calls this to get the DialogFragment's layout, regardless
//         * of whether it's being displayed as a dialog or an embedded fragment.
//         */
//
//        ImageView Im_Full;
//        RelativeLayout Item_Full_image_linear;
//
//        @Override
//        public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                                 Bundle savedInstanceState) {
//            // Inflate the layout to use as dialog or embedded fragment
//            return inflater.inflate(R.layout.item_full_image, container, false);
//        }
//
//        @Override
//        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//            super.onViewCreated(view, savedInstanceState);
//            Im_Full = (ImageView) view.findViewById(R.id.Im_Full);
//          //  Im_Full.setBackground(itemPic.getDrawable());
//            picasso.load("http://bubble.zeowls.com/uploads/" + item_image).into(Im_Full);
//
//            Item_Full_image_linear = (RelativeLayout) view.findViewById(R.id.Item_Full_image_linear);
//            Item_Full_image_linear.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                }
//            });
//
//            //  picasso.load("http://bubble.zeowls.com/uploads/" + item_image).fit().centerCrop().into(Im_Full);
//
//
//        }
//
//        /**
//         * The system calls this only when creating the layout in a dialog.
//         */
//        @Override
//        public Dialog onCreateDialog(Bundle savedInstanceState) {
//            // The only reason you might override this method when using onCreateView() is
//            // to modify any dialog characteristics. For example, the dialog includes a
//            // title by default, but your custom layout might not need it. So here you can
//            // remove the dialog title, but you must call the superclass to get the Dialog.
//            Dialog dialog = super.onCreateDialog(savedInstanceState);
//            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//            return dialog;
//        }
//    }


//    public void showDialog() {
//        final Bundle bundle = new Bundle();
//        bundle.putString("IMAGE_NAME", "http://bubble.zeowls.com/uploads/" + item_image);
//
//        FragmentManager fragmentManager = getFragmentManager();
//        CustomDialogFragment newFragment = new CustomDialogFragment();
//        newFragment.setArguments(bundle);
//
//
//        // The device is smaller, so show the fragment fullscreen
//        FragmentTransaction transaction = fragmentManager.beginTransaction();
//        // For a little polish, specify a transition animation
//        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//        // To make it fullscreen, use the 'content' root view as the container
//        // for the fragment, which is always the root view for the activity
//        transaction.add(android.R.id.content, newFragment)
//                .addToBackStack(null).commit();
//
//    }


    private class loadingData2 extends AsyncTask {

        @Override
        protected void onPreExecute() {
            items.clear();
        }

        @Override
        protected void onPostExecute(Object o) {
            mProgressBar.setVisibility(View.GONE);
            Item_Detail_Root_Layout.setVisibility(View.VISIBLE);
            item_detail_Shop_Top_Items.setAdapter(adapter);
        }

        @Override
        protected Object doInBackground(Object[] params) {

            try {
                Core core = new Core(getContext());
                JSONArray jsonArray = core.getShopItems(shop_id);
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

    private void updateUI() {


        if (item_Qty_String != null && !item_Qty_String.equals("null")) {
            item_Qty_String += " items in Stock";
            item_Qty.setText(item_Qty_String);
        }

        if (Shop_Short_Desc != null && !Shop_Short_Desc.equals("null")) {
            item_detail_shop_Short_Desc.setText(Shop_Short_Desc);

        } else {
            item_detail_shop_Short_Desc.setVisibility(View.GONE);
        }

        if (item_name != null && !item_name.equals("null")) {
            item_Detail_Name.setText(item_name);
        }

        if (item_desc != null && !item_desc.equals("null")) {
            item_detail_desc_2.setText(item_desc);
        }


        if (shop_name_txt != null && !shop_name_txt.equals("null")) {
            shopName.setText(shop_name_txt);
        }

        if (item_desc != null && !item_desc.equals("null")) {
            item_detail_desc_2.setText(item_desc);
        }


        if (item_desc != null && !item_desc.equals("null")) {
            description.setText(item_desc);
        }

        if (item_price != null && !item_price.equals("null")) {
            price.setText(item_price);
        }


        if (shop_name_txt != null && !shop_name_txt.equals("null")) {
            item_detail_shop_name_2.setText(shop_name_txt);
        }

        if (Shop_Address != null && !Shop_Address.equals("null")) {
            Log.d("Address", Shop_Address);
            item_detail_shop_Address.setText(Shop_Address);
        } else {
            item_detail_shop_Address.setVisibility(View.GONE);
        }



        picasso.load("http://bubble.zeowls.com/uploads/" + Shop_image).fit().centerCrop().into(item_Shop_Photo);
        picasso.load("http://bubble.zeowls.com/uploads/" + Shop_image).fit().centerCrop().into(item_Shop_Photo_2);
        picasso.load("http://bubble.zeowls.com/uploads/" + item_image).fit().centerCrop().into(itemPic);

        Expandable_Reviews.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {

                    @Override
                    public boolean onPreDraw() {
                        Expandable_Reviews.getViewTreeObserver().removeOnPreDrawListener(this);
                        Expandable_Reviews.setVisibility(View.GONE);

                        final int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                        final int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
//                        Expandable_Reviews.measure(widthSpec, heightSpec);

                        mAnimator = slideAnimator(0, Expandable_Reviews.getMeasuredHeight());
                        return true;
                    }
                });


        Reviews_Header.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (Expandable_Reviews.getVisibility() == View.GONE) {

                    expand();
                    Reviews_Arrow_Down.setVisibility(View.GONE);
                    Reviews_Arrow_Up.setVisibility(View.VISIBLE);
                } else {

                    collapse();
                    Reviews_Arrow_Down.setVisibility(View.VISIBLE);
                    Reviews_Arrow_Up.setVisibility(View.GONE);

                }
            }
        });


        Expandable_OverView.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {

                    @Override
                    public boolean onPreDraw() {
                        Expandable_OverView.getViewTreeObserver().removeOnPreDrawListener(this);
                        Expandable_OverView.setVisibility(View.GONE);

                        final int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                        final int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
//                        Expandable_Reviews.measure(widthSpec, heightSpec);

                        mAnimator2 = slideAnimator2(0, Expandable_OverView.getMeasuredHeight());
                        return true;
                    }
                });


        OverView_Header.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (Expandable_OverView.getVisibility() == View.GONE) {

                    expand2();
                    OverView_Arrow_Down.setVisibility(View.GONE);
                    OverView_Arrow_Up.setVisibility(View.VISIBLE);
                } else {

                    collapse2();
                    OverView_Arrow_Down.setVisibility(View.VISIBLE);
                    OverView_Arrow_Up.setVisibility(View.GONE);
                }
            }
        });


        Expandable_Details.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {

                    @Override
                    public boolean onPreDraw() {
                        Expandable_Details.getViewTreeObserver().removeOnPreDrawListener(this);
                        Expandable_Details.setVisibility(View.GONE);

                        final int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                        final int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
//                        Expandable_Reviews.measure(widthSpec, heightSpec);

                        mAnimator3 = slideAnimator3(0, Expandable_Details.getMeasuredHeight());
                        return true;
                    }
                });


        Details_Header.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (Expandable_Details.getVisibility() == View.GONE) {

                    expand3();
                    Details_Arrow_Down.setVisibility(View.GONE);
                    Details_Arrow_Up.setVisibility(View.VISIBLE);
                } else {

                    collapse3();
                    Details_Arrow_Down.setVisibility(View.VISIBLE);
                    Details_Arrow_Up.setVisibility(View.GONE);
                }
            }
        });


        itemPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //      showDialog();
            }
        });
        //  Fragment_Item_Detail_ScrollView.fullScroll(ScrollView.FOCUS_UP);


    }

}
