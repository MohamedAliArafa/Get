package com.zeowls.get.provider;

/**
 * Created by root on 4/20/16.
 */
import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class Contract {

    public static final String CONTENT_AUTHORITY = "com.zeowls.get";
    public static final Uri BASE_CONTENT = Uri.parse("content://"+ CONTENT_AUTHORITY);
    public static final String PATH_SHOPS = "shops";
    public static final String PATH_ITEMS = "items";
    public static final String PATH_CATS = "categories";
    public static final String PATH_PARENT_CATS = "parent_categories";
    public static final String PATH_CART = "cart";

    public static final class ShopEntry implements BaseColumns {
        public static Uri CONTENT_URI = BASE_CONTENT.buildUpon().appendPath(PATH_SHOPS).build();
        public static String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SHOPS;
        public static String TABLE_NAME = "shops";
        public static String COLUMN_ID = "id";
        public static String COLUMN_FAV = "fav";
        public static String COLUMN_NAME = "name";
        public static String COLUMN_OWNER_NAME = "owner_name";
        public static String COLUMN_EMAIL = "email";
        public static String COLUMN_MOBILE = "mobile";
        public static String COLUMN_DESCRIPTION = "description";
        public static String COLUMN_SHORT_DESCRIPTION = "short_description";
        public static String COLUMN_ADDRESS = "address";
        public static String COLUMN_PROFILE_PIC = "profile_pic";
        public static String COLUMN_COVER_PIC = "cover_pic";

        public static Uri BuildMovieUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI,id);
        }

    }

    public static final class ItemEntry implements BaseColumns {
        public static Uri CONTENT_URI = BASE_CONTENT.buildUpon().appendPath(PATH_ITEMS).build();
        public static String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ITEMS;
        public static String TABLE_NAME = "items";
        public static String COLUMN_ID = "id";
        public static String COLUMN_FAV = "fav";
        public static String COLUMN_SHOP_ID = "shop_id";
        public static String COLUMN_CAT_ID = "cat_id";
        public static String COLUMN_NAME = "name";
        public static String COLUMN_IMAGE = "image";
        public static String COLUMN_PRICE = "price";
        public static String COLUMN_DESCRIPTION = "description";

        public static Uri BuildItemUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI,id);
        }

        public static Uri BuildItemShopwUri(String shop){
            return CONTENT_URI.buildUpon().appendPath(shop).build();
        }

    }

    public static final class CategoryEntry implements BaseColumns {
        public static Uri CONTENT_URI = BASE_CONTENT.buildUpon().appendPath(PATH_CATS).build();
        public static String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CATS;
        public static String TABLE_NAME = "category";
        public static String COLUMN_ID = "id";
        public static String COLUMN_NAME = "name";
        public static String COLUMN_PARENT_ID = "parent_id";

        public static Uri BuildCatUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI,id);
        }

        public static Uri BuildCatParentwUri(String parent){
            return CONTENT_URI.buildUpon().appendPath(parent).build();
        }

    }

    public static final class ParentCategoryEntry implements BaseColumns {
        public static Uri CONTENT_URI = BASE_CONTENT.buildUpon().appendPath(PATH_PARENT_CATS).build();
        public static String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PARENT_CATS;
        public static String TABLE_NAME = "parent_category";
        public static String COLUMN_ID = "id";
        public static String COLUMN_NAME = "name";

        public static Uri BuildParentCatUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI,id);
        }

    }

    public static final class CartEntry implements BaseColumns {
        public static Uri CONTENT_URI = BASE_CONTENT.buildUpon().appendPath(PATH_CART).build();
        public static String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CART;
        public static String TABLE_NAME = "cart";
        public static String COLUMN_ITEM_ID = "item_id";
        public static String COLUMN_ITEM_NAME = "item_name";
        public static String COLUMN_ITEM_PRICE = "item_price";
        public static String COLUMN_ITEM_PHOTO = "item_photo";
        public static String COLUMN_ITEM_DESC = "item_desc";
        public static String COLUMN_SHOP_ID = "shop_id";
        public static String COLUMN_SHOP_NAME = "shop_name";

        public static Uri BuildParentCatUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI,id);
        }

    }
}
