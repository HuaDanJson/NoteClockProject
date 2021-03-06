package cool.android.greendao;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import cool.camerax.noteclockproject.bean.NoteBean;

import cool.android.greendao.NoteBeanDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig noteBeanDaoConfig;

    private final NoteBeanDao noteBeanDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        noteBeanDaoConfig = daoConfigMap.get(NoteBeanDao.class).clone();
        noteBeanDaoConfig.initIdentityScope(type);

        noteBeanDao = new NoteBeanDao(noteBeanDaoConfig, this);

        registerDao(NoteBean.class, noteBeanDao);
    }
    
    public void clear() {
        noteBeanDaoConfig.clearIdentityScope();
    }

    public NoteBeanDao getNoteBeanDao() {
        return noteBeanDao;
    }

}
