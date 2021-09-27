/*
 * Copyright (C) 2019 - 2019-2021 Vishnu Sanal. T
 *
 * This file is part of Quotes Status Creator.
 *
 * Quotes Status Creator is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package phone.vishnu.quotes.repository;

import android.app.Application;
import android.os.AsyncTask;
import androidx.lifecycle.LiveData;
import java.util.List;
import java.util.concurrent.ExecutionException;
import phone.vishnu.quotes.dao.FavDao;
import phone.vishnu.quotes.database.FavDatabase;
import phone.vishnu.quotes.model.Quote;

public class FavRepository {

    private final FavDao favDao;
    private final LiveData<List<Quote>> favList;

    public FavRepository(Application application) {
        FavDatabase database = FavDatabase.getInstance(application);
        favDao = database.favDao();
        favList = favDao.getAllFav();
    }

    public long insertFav(Quote quote) {
        try {
            return new InsertFavAsyncTask(favDao)
                    .execute(quote)
                    .get(); // TODO: FInd a proper way to do this
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void updateFav(Quote quote) {
        new UpdateFavAsyncTask(favDao).execute(quote);
    }

    public void deleteFav(Quote quote) {
        new DeleteFavAsyncTask(favDao).execute(quote);
    }

    public LiveData<List<Quote>> getAllFav() {
        return favList;
    }

    public boolean isPresent(Quote quote) {
        try {
            return new CheckPresentAsyncTask(favDao)
                    .execute(quote)
                    .get(); // TODO: FInd a proper way to do this
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void deleteAll() {
        new DeleteAllFavAsyncTask(favDao).execute();
    }

    private static class InsertFavAsyncTask extends AsyncTask<Quote, Void, Long> {
        private final FavDao favDao;

        public InsertFavAsyncTask(FavDao favDao) {
            this.favDao = favDao;
        }

        @Override
        protected Long doInBackground(Quote... fav) {

            Quote q = fav[0];

            q.setDateAdded(System.currentTimeMillis());

            return favDao.insert(q);
        }
    }

    private static class UpdateFavAsyncTask extends AsyncTask<Quote, Void, Void> {
        private final FavDao favDao;

        public UpdateFavAsyncTask(FavDao favDao) {
            this.favDao = favDao;
        }

        @Override
        protected Void doInBackground(Quote... fav) {
            favDao.update(fav[0]);
            return null;
        }
    }

    private static class DeleteFavAsyncTask extends AsyncTask<Quote, Void, Void> {
        private final FavDao favDao;

        public DeleteFavAsyncTask(FavDao favDao) {
            this.favDao = favDao;
        }

        @Override
        protected Void doInBackground(Quote... fav) {
            favDao.delete(fav[0]);
            return null;
        }
    }

    private static class DeleteAllFavAsyncTask extends AsyncTask<Void, Void, Void> {
        private final FavDao favDao;

        public DeleteAllFavAsyncTask(FavDao favDao) {
            this.favDao = favDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            favDao.deleteAll();
            return null;
        }
    }

    private static class CheckPresentAsyncTask extends AsyncTask<Quote, Void, Boolean> {
        private final FavDao favDao;

        public CheckPresentAsyncTask(FavDao favDao) {
            this.favDao = favDao;
        }

        @Override
        protected Boolean doInBackground(Quote... quotes) {
            return favDao.isPresent(quotes[0].getQuote(), quotes[0].getAuthor()) != 0;
        }
    }
}
