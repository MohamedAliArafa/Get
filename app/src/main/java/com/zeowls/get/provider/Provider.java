package com.zeowls.get.provider;

/**
 * Created by root on 4/20/16.
 */
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

public class Provider extends ContentProvider {

    static final int SHOPS = 100;
    static final int ITEMS = 101;
    static final int CAT = 102;
    static final int PARENT_CAT = 103;
    static final int CART = 104;

    private DBHelper mOpenHelper;
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    @Override
    public boolean onCreate() {
        mOpenHelper = new DBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            case SHOPS:
            {
                retCursor = mOpenHelper.getReadableDatabase().query(Contract.ShopEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            }
            case ITEMS: {
                retCursor = mOpenHelper.getReadableDatabase().query(Contract.ItemEntry.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
                break;
            }
            case CAT: {
                retCursor = mOpenHelper.getReadableDatabase().query(Contract.CategoryEntry.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
                break;
            }
            case PARENT_CAT: {
                retCursor = mOpenHelper.getReadableDatabase().query(Contract.ParentCategoryEntry.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
                break;
            }
            case CART: {
                retCursor = mOpenHelper.getReadableDatabase().query(Contract.CartEntry.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case SHOPS:
                return Contract.ShopEntry.CONTENT_TYPE;
            case ITEMS:
                return Contract.ItemEntry.CONTENT_TYPE;
            case CAT:
                return Contract.CategoryEntry.CONTENT_TYPE;
            case PARENT_CAT:
                return Contract.ParentCategoryEntry.CONTENT_TYPE;
            case CART:
                return Contract.CartEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;
        switch (match) {
            case SHOPS: {
                long _id = db.insert(Contract.ShopEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = Contract.ShopEntry.BuildMovieUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case ITEMS: {
                long _id = db.insert(Contract.ItemEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = Contract.ItemEntry.BuildItemUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case CAT: {
                long _id = db.insert(Contract.CategoryEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = Contract.CategoryEntry.BuildCatUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case PARENT_CAT: {
                long _id = db.insert(Contract.ParentCategoryEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = Contract.ParentCategoryEntry.BuildParentCatUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case CART: {
                long _id = db.insert(Contract.CartEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = Contract.CartEntry.BuildParentCatUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
//        db.close();
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int returnRow;
        if (selection == null) selection = "1";
        switch (match) {
            case SHOPS: {
                returnRow = db.delete(Contract.ShopEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            case ITEMS: {
                returnRow = db.delete(Contract.ItemEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            case CAT: {
                returnRow = db.delete(Contract.CategoryEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            case PARENT_CAT: {
                returnRow = db.delete(Contract.ParentCategoryEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            case CART: {
                returnRow = db.delete(Contract.CartEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (returnRow != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
//        db.close();
        return returnRow;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int returnRow;
        if (selection == null) selection = "1";
        switch (match) {
            case SHOPS: {
                returnRow = db.update(Contract.ShopEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            }
            case ITEMS: {
                returnRow = db.update(Contract.ItemEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            }
            case CAT: {
                returnRow = db.update(Contract.CategoryEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            }
            case PARENT_CAT: {
                returnRow = db.update(Contract.ParentCategoryEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            }
            case CART: {
                returnRow = db.update(Contract.CartEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (returnRow != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
//        db.close();
        return returnRow;
    }

    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = Contract.CONTENT_AUTHORITY;
        matcher.addURI(authority,Contract.PATH_SHOPS, SHOPS);
        matcher.addURI(authority,Contract.PATH_ITEMS, ITEMS);
        matcher.addURI(authority,Contract.PATH_CATS, CAT);
        matcher.addURI(authority,Contract.PATH_PARENT_CATS, PARENT_CAT);
        matcher.addURI(authority,Contract.PATH_CART, CART);
        return matcher;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case SHOPS:
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insertWithOnConflict(Contract.ShopEntry.TABLE_NAME, null, value, SQLiteDatabase.CONFLICT_IGNORE);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            default:
                return super.bulkInsert(uri, values);
        }
    }
}
