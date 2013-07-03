package com.socialbar.android.restapi.provider;

import static android.provider.BaseColumns._ID;
import static com.socialbar.android.restapi.provider.EstabelecimentosConstants.AUTHORITY;
import static com.socialbar.android.restapi.provider.EstabelecimentosConstants.CONTENT_URI;
import static com.socialbar.android.restapi.provider.EstabelecimentosConstants.TABLE_NAME;

import com.socialbar.android.restapi.util.Logger;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.TextUtils;


public class EstabelecimentosProvider extends ContentProvider {

	private static final String TAG = EstabelecimentosProvider.class.getSimpleName();

	private static final int ESTABELECIMENTOS = 1;
	private static final int ESTABELECIMENTOS_ID = 2;

	/**
	 * The MIME type of a directory of events
	 */
	private static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.example.estabelecimentos";

	/**
	 * The MIME type of a single event
	 */
	private static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.example.estabelecimentos";

	private ProviderDbHelper dbHelper;
	private UriMatcher uriMatcher;

	@Override
	public boolean onCreate() {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(AUTHORITY, "estabelecimentos", ESTABELECIMENTOS);
		uriMatcher.addURI(AUTHORITY, "estabelecimentos/#", ESTABELECIMENTOS_ID);
		this.dbHelper = new ProviderDbHelper(this.getContext());
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String orderBy) {
		if (uriMatcher.match(uri) == ESTABELECIMENTOS_ID) {
			long id = Long.parseLong(uri.getPathSegments().get(1));
			selection = appendRowId(selection, id);
		}

		// Get the database and run the query
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor cursor = db.query(TABLE_NAME, projection, selection,
				selectionArgs, null, null, orderBy);

		// Tell the cursor what uri to watch, so it knows when its
		// source data changes
		cursor.setNotificationUri(getContext().getContentResolver(), uri);
		return cursor;
	}

	@Override
	public String getType(Uri uri) {
		switch (uriMatcher.match(uri)) {
		case ESTABELECIMENTOS:
			return CONTENT_TYPE;
		case ESTABELECIMENTOS_ID:
			return CONTENT_ITEM_TYPE;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();

		// Validate the requested uri
		if (uriMatcher.match(uri) != ESTABELECIMENTOS) {
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		// Insert into database
		long id = db.insertOrThrow(TABLE_NAME, null, values);

		// Notify any watchers of the change
		Uri newUri = ContentUris.withAppendedId(CONTENT_URI, id);

		Logger.debug(TAG, "New estabelecimentos URI: " + newUri.toString());

		getContext().getContentResolver().notifyChange(newUri, null);
		return newUri;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {

		throw new UnsupportedOperationException("not implemented");
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {

		SQLiteDatabase db = dbHelper.getWritableDatabase();

		// Validate the requested uri
		if (uriMatcher.match(uri) != ESTABELECIMENTOS_ID) {
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		String recordId = Long.toString(ContentUris.parseId(uri));
		int affected = db.update(TABLE_NAME, values, BaseColumns._ID
				+ "="
				+ recordId
				+ (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')'
						: ""), selectionArgs);

		Logger.debug(TAG, "Updated estabelecimentos URI: " + uri.toString());

		getContext().getContentResolver().notifyChange(uri, null);
		return affected;
	}

	/**
	 * Append an id test to a SQL selection expression
	 */
	private String appendRowId(String selection, long id) {
		return _ID
				+ "="
				+ id
				+ (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')'
						: "");
	}

}
