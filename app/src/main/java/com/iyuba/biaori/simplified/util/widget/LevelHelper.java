package com.iyuba.biaori.simplified.util.widget;

import android.util.Log;

import com.iyuba.biaori.simplified.Constant;


/**
 * Created by maoyujiao on 2019/3/23.
 */

public class LevelHelper {
    /**
     * 记录 每个等级的单词库的 起始id -1，也就是该单词库的最小id -1
     * 例如 nlevel=1    LEVEL_GATEWAY_BEGIN【0】=0
     * nlevel为1时 LEVEL_GATEWAY_BEGIN【nlevel - 1 】 = id -1
     */
    public static final int[] LEVEL_GATEWAY_BEGIN = new int[100];
    /**
     * 记录 每个等级的单词库的 起始id，也就是该单词库的最小id
     * 例如 nlevel=1    LEVEL_GATEWAY_BEGIN【0】=0
     * nlevel为1时 LEVEL_GATEWAY_BEGIN【nlevel - 1 】 = id
     * <p>
     * LEVEL_GATEWAY_BEGIN_IDRECORD【nlevel -1】 -1=LEVEL_GATEWAY_BEGIN【nlevel -1】
     */
    public static final int[] LEVEL_GATEWAY_BEGIN_IDRECORD = new int[100];
    /**
     * 根据nlevel 记录每个单词库的单词总数，需要经过 levelHelper.initHelper()之后才有数据
     * 例如 nlevel = 1 时，单词库为n1   LEVEL_GATEWAY_WORD_TOTAL【0】 代表 n1单词库的单词总数
     * LEVEL_GATEWAY_WORD_TOTAL【nlevel -1】 是 nlevel所代表的的单词库的单词总数
     */
    public static final int[] LEVEL_GATEWAY_WORD_TOTAL = new int[100];

//
//    /**
//     * 获取 gameView总的关数
//     *
//     * @return
//     */
//    public static int[] getGatewayCount() {
//        int[] gatewayCount = new int[LEVEL_GATEWAY_WORD_TOTAL.length];
//        int levelNum = getLevelWordNum();
//        for (int i = 0; i < LEVEL_GATEWAY_WORD_TOTAL.length; i++) {
//            gatewayCount[i] = LEVEL_GATEWAY_WORD_TOTAL[i] % levelNum == 0 ? LEVEL_GATEWAY_WORD_TOTAL[i] / levelNum : LEVEL_GATEWAY_WORD_TOTAL[i] / levelNum + 1;
//        }
//        return gatewayCount;
//    }
//
//    public static String getRightRadio(int itemPos) {
//        List<Word> list = dao.getWord(itemPos);
//        int rightCount = 0;
//        for (Word word : list) {
//            if (word.getUserAnswer() == 1) {
//                rightCount++;
//            }
//        }
//        String ratio = String.format("%d/%d", rightCount, list.size());//rightCount+"/"+list.size();
//        return ratio;
//    }
//
//    /**
//     * 切换每关单词个数后，已闯关个数会改变
//     *
//     * @param levelNum
//     */
//    public static void setCurrentLevel(int levelNum) {
//        int nLevel = SharedDaoHleper.getNlevel();
//        String name = SPUtil.getLevelWordKey(nLevel);
//
//        int sum = dao.getAnsweredWordSum(nLevel);
//        Log.e("wangwenyang", sum + "");
//        int curLevel = sum / levelNum + 1;
//
//        SPUtil.Instance().putInt(name, curLevel);
//    }
//
//    public static void resetCurrentLevel() {
//        for (int i = 1; i < 100; i++) {
//            String name = SPUtil.getLevelWordKey(1);
//            SPUtil.Instance().putInt(name, 1);
//        }
//    }
//
//    /**
//     * 获取标日课本对应的单词等级
//     *
//     * @return
//     */
//    public static int getBiaoriLevel() {
//        int textBook = SPUtil.Instance().loadInt(SPUtil.SP_LESSON_WHICH);
//        for (ShowKeben.DataBean dataBean : BookDao.getInstance().queryAllBook()) {
//            if (textBook == dataBean.getSource()) {
//                return dataBean.getLevel();
//            }
//        }
//        return 6;
//    }
//
//
//    /**
//     * 获取设置的每关单词个数
//     *
//     * @return
//     */
//    public static int getLevelWordNum() {
//        return Constant.WORD_QUANTITY;
//    }
//
//    /**
//     * 获取单词总数
//     */
//    public static int getTotalWordNum() {
//        int nLevel = SharedDaoHleper.getNlevel();
//        return LevelHelper.LEVEL_GATEWAY_WORD_TOTAL[nLevel - 1];
//    }
//
//    /**
//     * 获取单词总数
//     */
//    public static int getTotalWordNum(int nLevel) {
//        return LevelHelper.LEVEL_GATEWAY_WORD_TOTAL[nLevel - 1];
//    }
//
//    /**
//     * 利用wordDao层的数据来更新 内存中的记录数据
//     */
//    public static void initLevelHelper() {
//        synchronized (LevelHelper.class) {
//            WordDao dao = new WordDao(null);
//            for (int i = 0; i < LEVEL_GATEWAY_WORD_TOTAL.length; i++) {
//                if (LEVEL_GATEWAY_WORD_TOTAL[i] == 0) {
//                    LEVEL_GATEWAY_WORD_TOTAL[i] = dao.getLevelTotal(i + 1);
//                }
//
//                if (LEVEL_GATEWAY_BEGIN[i] == 0) {
//                    LEVEL_GATEWAY_BEGIN[i] = dao.getLevelBegin(i + 1);
//                }
//
//                if (LEVEL_GATEWAY_BEGIN_IDRECORD[i] == 0) {
//                    LEVEL_GATEWAY_BEGIN_IDRECORD[i] = dao.getLevelBeginIdRecord(i + 1);
//                }
//            }
//        }
//    }
//
//    /**
//     * 利用wordDao层的数据来更新 内存中的记录数据
//     */
//    public static void initLevelHelperOneLevel(int nlevel) {
//        synchronized (LevelHelper.class) {
//            WordDao dao = new WordDao(null);
//            if (LEVEL_GATEWAY_WORD_TOTAL[nlevel - 1] == 0) {
//                LEVEL_GATEWAY_WORD_TOTAL[nlevel - 1] = dao.getLevelTotal(nlevel);
//            }
//
//            if (LEVEL_GATEWAY_BEGIN[nlevel - 1] == 0) {
//                LEVEL_GATEWAY_BEGIN[nlevel - 1] = dao.getLevelBegin(nlevel);
//            }
//
//            if (LEVEL_GATEWAY_BEGIN_IDRECORD[nlevel - 1] == 0) {
//                LEVEL_GATEWAY_BEGIN_IDRECORD[nlevel - 1] = dao.getLevelBeginIdRecord(nlevel);
//            }
//        }
//    }
}
