package com.example.demo.util;
import com.example.demo.config.SpringBootBeanAutowiringSupport;
import com.example.demo.entity.Page;
import com.mongodb.MongoException;
import com.mongodb.WriteResult;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.util.Assert;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 * @author linzhiqiang
 * @create 2018/08/01
 */
public class MongoUtil extends SpringBootBeanAutowiringSupport {

    private static final Logger LOGGER = LoggerFactory.getLogger(MongoUtil.class);

    /**
     * &符号
     */
    private static final String REGEX = "&";
    /**
     * 等于
     */
    private static final String EQ = "eq";
    /**
     * 大于
     */
    private static final String GT = "gt";
    /**
     * 小于
     */
    private static final String LT = "lt";
    /**
     * 大于等于
     */
    private static final String GTE = "gte";
    /**
     * 小于等于
     */
    private static final String LTE = "lte";
    /**
     * 模糊
     */
    private static final String LIKE = "like";
    /**
     * 不为空
     */
    private static final String NOT = "not";
    /**
     * 不等于
     */
    private static final String NE = "ne";
    /**
     * in()
     */
    private static final String IN = "in";
    /**
     * not in ()
     */
    private static final String NIN = "nin";

    /**
     * 默认下标
     */
    private static final int CRITERIA_INDEX = -1;
    /**
     * 自动装配注解会让Spring通过类型匹配为beanFactory注入示例
     */
    @Autowired
    private BeanFactory beanFactory;

    private static MongoUtil instance;

    @Autowired
    private MongoTemplate mongoTemplate;

    static {
        instance = new MongoUtil();
    }

    /**
     * 返回当前实例
     *
     * @return
     */
    public static MongoUtil getInstance() {
        return instance;
    }

    /**
     * 保存对象
     *
     * @param object
     */
    public void save(Object object) throws MongoException {
        mongoTemplate.save(object);
    }

    /**
     * 根据ID获取一个对象
     *
     * @param id id
     * @param c  当前对象类，请使用(Attention.class)
     * @return
     */
    public Object findOne(String id, Class c) throws MongoException {
        //BasicDBObject queryWhele = new BasicDBObject();
        //queryWhele.put("id", id);
        // return mongoTemplate.findOne(new BasicQuery(queryWhele), c);
        return mongoTemplate.findOne(new Query(where("id").is(id)), c);
    }

    /**
     * 查询实体所有数据
     *
     * @param c 当前对象类，请使用(Attention.class)
     * @return
     */
    public List findAll(Class c) throws MongoException {
        return mongoTemplate.findAll(c);
    }

    /**
     * 分页查询数据
     *
     * @param paramMap 条件参数(如果需要排序添加 $sort$ 参数，值规则：updateTime:desc,socre:asc,如果有or操作，请用#or#，如key值为:#or#name)
     * @param index    当前面
     * @param size     总页数
     * @param c        当前对象类，请使用(Attention.class)
     * @return 结果集
     */
    public Map<String, Object> listPageByParam(Map<String, Object> paramMap, int index, int size, Class c) throws MongoException {
        Map<String, Object> resultMap = new HashMap<>(3);
        index = index - 1;
        if (index < 0) {
            index = 0;
        }
        if (size < 0) {
            size = 10;
        }
        Query query = getQuery(paramMap);

        /**query.skip("从多少条").limit("一页的数据条数");分页*/
        long count = mongoTemplate.count(query, c);
        long totalPage = (count % size == 0) ? (count / size) : (count / size) + 1;

        query.skip(index * size).limit(size);
        List list = mongoTemplate.find(query, c);
        resultMap.put("data", list);
        resultMap.put("totalPage", totalPage);
        resultMap.put("totalCount", count);
        return resultMap;
    }

    /**
     * 分页查询数据
     *
     * @param paramMap 条件参数(如果需要排序添加 $sort$ 参数，值规则：updateTime:desc,socre:asc,如果有or操作，请用#or#，如key值为:#or#name)
     * @param index    起始
     * @param size     条数
     * @param c        当前对象类，请使用(Attention.class)
     * @return 结果集
     */
    public Map<String, Object> listPageByBeginEnd(Map<String, Object> paramMap, int index, int size, Class c) throws MongoException {
        Map<String, Object> resultMap = new HashMap<>(3);
        if (index < 0) {
            index = 0;
        }
        if (size < 0) {
            size = 10;
        }
        Query query = getQuery(paramMap);

        /**query.skip("从多少条").limit("一页的数据条数");分页*/
        long count = mongoTemplate.count(query, c);
        long totalPage = (count % ((index==2&&size==8)?10:size) == 0) ? (count / ((index==2&&size==8)?10:size)) : (count / ((index==2&&size==8)?10:size)) + 1;

        query.skip(index).limit(size);
        List list = mongoTemplate.find(query, c);
        resultMap.put("data", list);
        resultMap.put("totalPage", totalPage);
        resultMap.put("totalCount", count);
        return resultMap;
    }

    /**
     * 分页查询数据
     *
     * @param paramMap 条件参数(如果需要排序添加 $sort$ 参数，值规则：updateTime:desc,socre:asc,如果有or操作，请用#or#，如key值为:#or#name)
     * @param page     分页实体(pageNo当前面请默认为1，)
     * @param c
     * @return
     */
    public List listPage(Map<String, Object> paramMap, Page page, Class c) {
        Query query = getQuery(paramMap);
        long count = mongoTemplate.count(query, c);
        page.setTotalRecord(count);
        int pageNo = page.getPageNo() - 1;
        if (pageNo < 0) {
            pageNo = 0;
        }
        query.skip(pageNo * page.getPageSize()).limit(page.getPageSize());
        return mongoTemplate.find(query, c);
    }


    /**
     * 根据条件查询数据
     *
     * @param paramMap 条件参数(如果需要排序添加 $sort$ 参数，值规则：updateTime:desc,socre:asc,如果有or操作，请用#or#，如key值为:#or#name)
     * @param c        当前对象类，请使用(Attention.class)
     * @return
     */
    public List listByParamMap(Map<String, Object> paramMap, Class c) throws MongoException {
        return mongoTemplate.find(getQuery(paramMap), c);
    }

    /**
     * 获取总记录数
     *
     * @param paramMap 条件参数(如果需要排序添加 $sort$ 参数，值规则：updateTime:desc,socre:asc)
     * @param c        当前对象类，请使用(Attention.class)
     * @return 总记录数
     */
    public long countByParamMap(Map<String, Object> paramMap, Class c) throws MongoException {
        return mongoTemplate.count(getQuery(paramMap), c);
    }

    /**
     * 修改数据
     *
     * @param obj 实体对象
     * @param c   当前对象类，请使用(Attention.class)
     * @return >0 则返回成功
     */
    public int update(Object obj, Class c) throws MongoException {
        Map<String, Object> fields = getFields(obj);
        if (fields.size() > 0) {
            String id = (String) fields.get("id");
            if (StringUtils.isBlank(id)) {
                return 0;
            }
            Criteria criteria = where("id").is(id);
            fields.remove("id");
            Query query = new Query(criteria);
            Update update = new Update();
            for (String key : fields.keySet()) {
                update.set(key, fields.get(key));
            }

            WriteResult result = mongoTemplate.updateFirst(query, update, c);
            if (result != null) {
                return result.getN();
            }
        }
        return 0;
    }

    /**
     * 多条件修改数据
     *
     * @param updateMap 需要更新的键值对（id会过滤掉，禁止更新主键）
     * @param c   当前对象类，请使用(Attention.class)
     * @param param   条件参数（批量）
     * @return >0 则返回成功
     */
    public int updateByParam(Map<String,Object> updateMap, Class c, Map<String,Object> param) throws MongoException {
        Map<String, Object> fields = updateMap;
        if (fields.size() > 0) {
            fields.remove("id");
            Query query = getQuery(param);
            Update update = new Update();
            for (String key : fields.keySet()) {
                update.set(key, fields.get(key));
            }

            WriteResult result = mongoTemplate.updateMulti(query, update, c);
            if (result != null) {
                return result.getN();
            }
        }
        return 0;
    }

    /**
     * 根据ID删除
     *
     * @param id id
     * @param c  当前对象类，请使用(Attention.class)
     * @return 删除成功返回 删除总数
     */
    public int remove(String id, Class c) throws MongoException {
        return remove(new Query(where("id").in(id)), c);
    }

    /**
     * 根据条件删除(指删除数据)
     *
     * @param params
     * @param c
     * @return
     */
    public int removeBatch(Map<String, Object> params, Class c) {
        return remove(getQuery(params), c);
    }

    /**
     * 删除数据
     *
     * @param query Query
     * @param c
     * @return
     */
    private int remove(Query query, Class c) {
        WriteResult result = mongoTemplate.remove(query, c);
        if (result != null) {
            return result.getN();
        }
        return 0;
    }


    /**
     * 删除表
     *
     * @param c 当前对象类，请使用(Attention.class)
     */
    public void dropDatabase(Class c) throws MongoException {
        mongoTemplate.dropCollection(c);
    }

    /**
     * 根据表名删除
     *
     * @param collectionName
     */
    public void dropDatabase(String collectionName) throws MongoException {
        if (mongoTemplate.getCollectionNames().contains(collectionName)) {
            mongoTemplate.dropCollection(collectionName);
        }
    }


    /**
     * 聚合分组
     * 此聚合分组如同
     * sql=select userId,sum(scoreNumber) scoreNumberSum,avg(scoreNumber) scoreNumberAvg
     * ,min(scoreNumber) scoreNumberMin,max(scoreNumber) scoreNumber Max from table_name
     * where param1=param1 and param2=param2
     * order by updateTime desc,score asc
     * group by userId
     *
     * @param c          实体类
     * @param params     参数列表(说明：$sort$添加参数：
     *                   使用排序字段值规则为：
     *                   updateTime:desc,socre:asc)如：param.put("$sort$", "updateTime:desc,socre:asc");
     *                   使用分组$group$(可以是一个字符串，也可以是一个list集合,也可以是一个字符串数组)
     *                   需要统计的字段如： param.put("$group$", "scoreNumber&sum");
     * @param groupNames 分组字段列表  如根据用户id分组 userId
     * @param index      分页下标页(如果查询所有-1)
     * @param size       总页数(如果查询所有-1)
     * @return
     */
    public List groupByPage(Class c, Map<String, Object> params, String[] groupNames, int index, int size) {
        Assert.notNull(groupNames);
        List<AggregationOperation> aggregationOperations = new ArrayList<>(2);
        //获取排序字段
        String sort = (String) params.get("$sort$");
        if (StringUtils.isNotBlank(sort)) {
            params.remove("$sort$");
        }
        List<String> groupParam = getGroupParams(params);
        //查询条件
        MatchOperation matchOperation = getGroupMatchOperation(params);
        if (matchOperation != null) {
            aggregationOperations.add(matchOperation);
        }
        // 分组操作，对总条数进行统计
        GroupOperation groupOperation = getGroupOperation(groupParam, groupNames);
        aggregationOperations.add(groupOperation);

        // 排序
        SortOperation sortOperation = getGroupSortOperation(sort);

        if (sortOperation != null) {
            aggregationOperations.add(sortOperation);
        }

        //分页开始值
        if (index >= 0) {
            aggregationOperations.add(Aggregation.skip(index));
        }

        //分页总数
        if (size != -1) {
            aggregationOperations.add(Aggregation.limit(size));
        }
        //组合条件
        TypedAggregation aggregation = Aggregation.newAggregation(c, aggregationOperations);
        //使用Map集合方式输出
        AggregationResults<Map> results = mongoTemplate.aggregate(aggregation, Map.class);

        return results.getMappedResults();
    }

    /**
     * 获取聚合参数
     *
     * @param params
     * @return
     */
    private List<String> getGroupParams(Map<String, Object> params) {
        List<String> groupParam = new ArrayList<>(1);
        Object groupObject = params.get("$group$");
        if (groupObject != null) {
            params.remove("$group$");
            if (groupObject instanceof String) {
                groupParam.add((String) groupObject);
            } else if (groupObject instanceof List) {
                groupParam = (List<String>) groupObject;
            } else if (groupObject instanceof String[]) {
                groupParam = Arrays.asList((String[]) groupObject);
            }
        }
        return groupParam;
    }

    /**
     * 聚合分组
     * 此聚合分组如同
     * sql=select userId,sum(scoreNumber) scoreNumberSum,avg(scoreNumber) scoreNumberAvg
     * ,min(scoreNumber) scoreNumberMin,max(scoreNumber) scoreNumber Max from table_name
     * where param1=param1 and param2=param2
     * order by updateTime desc,score asc
     * group by userId
     *
     * @param c         实体类
     * @param params    参数列表(说明：$sort$添加参数：使用排序字段值规则为：updateTime:desc,socre:asc)如：param.put("$sort$", "updateTime:desc,socre:asc");
     *                  使用分组$group$需要统计的字段如： param.put("$group$", "scoreNumber");
     * @param groupName 分组字段列表  如根据用户id分组 userId
     * @return
     * @throws MongoException
     */
    public List getGroup(Class c, Map<String, Object> params, String... groupName) throws MongoException {
        Assert.notNull(groupName);
        //获取排序字段
        String sort = (String) params.get("$sort$");
        if (StringUtils.isNotBlank(sort)) {
            params.remove("$sort$");
        }
        String groupStr = (String) params.get("$group$");
        if (StringUtils.isNotBlank(groupStr)) {
            params.remove("$group$");
        }
        //查询条件
        MatchOperation matchOperation = getGroupMatchOperation(params);


        // 分组操作，对总条数进行统计
        GroupOperation groupOperation = getGroupOperation(groupStr, groupName);

        // 排序
        SortOperation sortOperation = getGroupSortOperation(sort);

        // 组合条件
        TypedAggregation aggregation = Aggregation.newAggregation(c, groupOperation);
        if (sortOperation != null && matchOperation != null) {
            aggregation = Aggregation.newAggregation(c, matchOperation, groupOperation, sortOperation);
        } else if (sortOperation != null) {
            aggregation = Aggregation.newAggregation(c, groupOperation, sortOperation);
        } else if (matchOperation != null) {
            aggregation = Aggregation.newAggregation(c, matchOperation, groupOperation);
        }
        //使用Map集合方式输出
        AggregationResults<Map> results = mongoTemplate.aggregate(aggregation, Map.class);

        return results.getMappedResults();
    }

    /**
     * 分组
     *
     * @param groupStr
     * @param groupName
     * @return
     */
    private GroupOperation getGroupOperation(String groupStr, String[] groupName) {
        GroupOperation groupOperation = null;
        if (StringUtils.isNotBlank(groupStr)) {
            groupOperation = Aggregation.group(groupName)
                    .count().as("count")
                    .sum(groupStr).as(groupStr + "Sum")
                    .avg(groupStr).as(groupStr + "Avg")
                    .min(groupStr).as(groupStr + "Min")
                    .max(groupStr).as(groupStr + "Max");
            groupOperation.sum("").as("");
        } else {
            groupOperation = Aggregation.group(groupName)
                    .count().as("count");
        }
        return groupOperation;
    }

    /**
     * 分组
     *
     * @param groupParams 需要统计的字段&类型：如(username&sum)
     * @param groupName
     * @return
     */
    private GroupOperation getGroupOperation(List<String> groupParams, String[] groupName) {

        GroupOperation groupOperation = null;
        if (groupParams == null || groupParams.isEmpty()) {
            groupOperation = Aggregation.group(groupName);
        } else {
            for (String group : groupParams) {
                if (StringUtils.isNotBlank(group)) {
                    String[] split = group.split("&");
                    if (split != null && split.length > 1) {
                        if ("avg".equalsIgnoreCase(split[1])) {
                            groupOperation = (groupOperation == null) ? Aggregation.group(groupName).avg(split[0]).as(split[0] + "Svg")
                                    : groupOperation.avg(split[0]).as(split[0] + "Svg");
                        } else if ("min".equalsIgnoreCase(split[1])) {
                            groupOperation = (groupOperation == null) ? Aggregation.group(groupName).min(split[0]).as(split[0] + "Min")
                                    : groupOperation.min(split[0]).as(split[0] + "Min");
                        } else if ("max".equalsIgnoreCase(split[1])) {
                            groupOperation = (groupOperation == null) ? Aggregation.group(groupName).max(split[0]).as(split[0] + "Max")
                                    : groupOperation.max(split[0]).as(split[0] + "Max");
                        } else if ("sum".equalsIgnoreCase(split[1])) {
                            groupOperation = (groupOperation == null) ? Aggregation.group(groupName).sum(split[0]).as(split[0] + "Sum")
                                    : groupOperation.sum(split[0]).as(split[0] + "Sum");
                        } else {
                            groupOperation = (groupOperation == null) ? Aggregation.group(groupName)
                                    .count().as(split[0] + "Count") : groupOperation.count().as(split[0] + "Count");

                        }
                    } else if (split != null && split.length == 1) {
                        groupOperation = (groupOperation == null) ? Aggregation.group(groupName)
                                .count().as(split[0] + "Count") : groupOperation.count().as(split[0] + "Count");

                    }
                }
            }
            if (groupOperation == null) {
                groupOperation = Aggregation.group(groupName);
            }
        }
        return groupOperation;
    }

    /**
     * 分组排序
     *
     * @param sort
     * @return
     */
    private SortOperation getGroupSortOperation(String sort) {
        List<Sort> sortList = getSort(sort);
        SortOperation sortOperation = null;
        for (Sort s : sortList) {
            if (sortOperation == null) {
                sortOperation = Aggregation.sort(s);
            } else {
                sortOperation.and(s);
            }
        }
        return sortOperation;
    }

    /**
     * 获取分组查询条件
     *
     * @param params
     * @return
     */
    private MatchOperation getGroupMatchOperation(Map<String, Object> params) {
        //查询条件
        Criteria criteria = getCriteria(params);

        MatchOperation matchOperation = null;
        if (criteria != null) {
            matchOperation = Aggregation.match(criteria);
        }
        return matchOperation;
    }


    /**
     * 根据实体获取field 和value
     *
     * @param object 当前对象
     * @return map集合
     */
    public Map<String, Object> getFields(Object object) throws MongoException {
        Class<?> aClass = object.getClass();
        //获取对象所有字段
        Map<String, Object> fieldMap = new HashMap<String, Object>(5);
        Object o = null;
        try {
            Field[] fields = object.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                o = field.get(object);
                if (o != null) {
                    fieldMap.put(field.getName(), field.get(object));
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage());
            throw new MongoException(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage());
            throw new MongoException(e.getMessage());
        }
        return fieldMap;
    }

    /**
     * 获取查询Query
     *
     * @param paramMap 查询参数
     * @return
     */
    private Query getQuery(Map<String, Object> paramMap) throws MongoException {

        String sort = (String) paramMap.get("$sort$");
        paramMap.remove("$sort$");
        Query query = new Query();
        query.addCriteria(getCriteria(paramMap));

        //排序
        List<Sort> sorts = getSort(sort);
        for (Sort s : sorts) {
            query.with(s);
        }
        return query;
    }

    /**
     * 获取Criteria
     *
     * @param params
     * @return
     */
    private Criteria getCriteria(Map<String, Object> params) {

        Criteria criteria = new Criteria();
        Object orMap = params.get("#or#");
        params.remove("#or#");
        if (!params.isEmpty()) {
            criteria.andOperator(getCriteriaList(params));
        }
        if (orMap != null) {
            Map<String, Object> orParams = (Map<String, Object>) orMap;
            if (!orParams.isEmpty()) {
                criteria.orOperator(getCriteriaList(orParams));
            }
        }
        return criteria;
    }

    /**
     * 获取条件列表
     *
     * @param params
     * @return
     */
    private Criteria[] getCriteriaList(Map<String, Object> params) {
        String[] split = null;
        Criteria addCriteria = null;

        List<Criteria> lists = new ArrayList<>();
        for (String key : params.keySet()) {
            if (StringUtils.isNotBlank(key)) {
                split = key.split(REGEX);
                addCriteria = addQueryCriteria(split, key, params);
                if (addCriteria != null) {
                    lists.add(addCriteria);
                }
            }
        }
        return lists.toArray(new Criteria[lists.size()]);
    }

    /**
     * 组装Query
     *
     * @param split  字段和查询符号
     * @param params 值
     */
    private Criteria addQueryCriteria(String[] split, String key, Map<String, Object> params) throws MongoException {
        Object obj = params.get(key);
        if (obj == null) {
            return null;
        }
        if (split == null) {
            return null;
        }
        if (split.length == 1) {
            return where(split[0]).is(obj);
        } else {
            Object obj1 = null;
            int type = getType(obj);
            String with = split[1];
            if (EQ.equalsIgnoreCase(with)) {
                return where(split[0]).is(obj);
            } else if (GT.equalsIgnoreCase(with) && obj != null && (obj1 = params.get(split[0] + REGEX + LT)) != null) {
                params.put(split[0] + REGEX + LT, null);
                return where(split[0]).gt(obj).lt(obj1);
            } else if (GT.equalsIgnoreCase(with) && obj != null && (obj1 = params.get(split[0] + REGEX + LTE)) != null) {
                params.put(split[0] + REGEX + LTE, null);
                return where(split[0]).gt(obj).lte(obj1);
            } else if (GT.equalsIgnoreCase(with)) {
                return where(split[0]).gt(obj);
            } else if (LT.equalsIgnoreCase(with) && obj != null && (obj1 = params.get(split[0] + REGEX + GT)) != null) {
                params.put(split[0] + REGEX + GT, null);
                return where(split[0]).gt(obj1).lt(obj);
            } else if (LT.equalsIgnoreCase(with) && obj != null && (obj1 = params.get(split[0] + REGEX + GTE)) != null) {
                params.put(split[0] + REGEX + GTE, null);
                return where(split[0]).gte(obj1).lt(obj);
            } else if (LT.equalsIgnoreCase(with)) {
                return where(split[0]).lt(obj);
            } else if (GTE.equalsIgnoreCase(with) && obj != null && (obj1 = params.get(split[0] + REGEX + LT)) != null) {
                params.put(split[0] + REGEX + LT, null);
                return where(split[0]).gte(obj).lt(obj1);
            } else if (GTE.equalsIgnoreCase(with) && obj != null && (obj1 = params.get(split[0] + REGEX + LTE)) != null) {
                params.put(split[0] + REGEX + LTE, null);
                return where(split[0]).gte(obj).lte(obj1);
            } else if (GTE.equalsIgnoreCase(with)) {
                return where(split[0]).gte(obj);
            } else if (LTE.equalsIgnoreCase(with) && obj != null && (obj1 = params.get(split[0] + REGEX + GT)) != null) {
                params.put(split[0] + REGEX + GT, null);
                return where(split[0]).gt(obj1).lte(obj);
            } else if (LTE.equalsIgnoreCase(with) && obj != null && (obj1 = params.get(split[0] + REGEX + GTE)) != null) {
                params.put(split[0] + REGEX + GTE, null);
                return where(split[0]).gte(obj1).lte(obj);
            } else if (LTE.equalsIgnoreCase(with)) {
                return where(split[0]).lte(obj);
            } else if (LIKE.equalsIgnoreCase(with) && type == 1) {
                String str = (String) obj;
                return where(split[0]).regex(".*?" + str + ".*");
            } else if (NOT.equalsIgnoreCase(with)) {
                return where(split[0]).not();
            } else if (NE.equalsIgnoreCase(with)) {
                if ("null".equals(obj.toString())) {
                    return where(split[0]).ne("").ne(null);
                } else {
                    return where(split[0]).ne(obj);
                }
            } else if (IN.equalsIgnoreCase(with) || NIN.equalsIgnoreCase(with)) {
                switch (type) {
                    case 4:
                        if (NIN.equalsIgnoreCase(with)) {
                            return where(split[0]).nin((Set) obj);
                        }
                        return where(split[0]).in((Set) obj);
                    case 5:
                        if (NIN.equalsIgnoreCase(with)) {
                            return where(split[0]).nin((List) obj);
                        }
                        return where(split[0]).in((List) obj);
                    case 6:
                    case 7:
                    case 8:
                        if (NIN.equalsIgnoreCase(with)) {
                            return where(split[0]).nin((Object[]) obj);
                        }
                        return where(split[0]).in((Object[]) obj);
                    //case 7:
                    //    if (NIN.equalsIgnoreCase(with)) {
                    //        return where(split[0]).nin((String[]) obj);
                    //    }
                    //    return where(split[0]).in((String[]) obj);
                    //case 8:
                    //    if (NIN.equalsIgnoreCase(with)) {
                    //        return where(split[0]).nin((Integer[]) obj);
                    //    }
                    //    return where(split[0]).in((Integer[]) obj);
                    default:
                        if (NIN.equalsIgnoreCase(with)) {
                            return where(split[0]).nin(obj);
                        }
                        return where(split[0]).in(obj);
                }
            } else {
                return where(split[0]).is(obj);
            }
        }

    }

    /**
     * 排序
     *
     * @param sort
     * @return
     */
    private List<Sort> getSort(String sort) {
        List<Sort> sorts = new ArrayList<>(5);
        if (StringUtils.isNotBlank(sort)) {
            String[] sortSplit = sort.split(",");
            for (String so : sortSplit) {
                String[] de = so.split(":");
                if (de.length == 2) {
                    if ("desc".equalsIgnoreCase(de[1])) {
                        sorts.add(new Sort(new Sort.Order(Sort.Direction.DESC, de[0])));
                    } else {
                        sorts.add(new Sort(new Sort.Order(Sort.Direction.ASC, de[0])));
                    }
                } else if (de.length == 1) {
                    sorts.add(new Sort(new Sort.Order(Sort.Direction.ASC, de[0])));
                }
            }
        }
        return sorts;
    }

    /**
     * 获取值类型
     *
     * @param obj
     * @return
     */
    private static int getType(Object obj) {
        if (obj instanceof String) {
            return 1;
        } else if (obj instanceof Integer || obj instanceof Long) {
            return 2;
        } else if (obj instanceof Date) {
            return 3;
        } else if (obj instanceof Set) {
            return 4;
        } else if (obj instanceof List) {
            return 5;
        } else if (obj instanceof Object[]) {
            return 6;
        } else if (obj instanceof String[]) {
            return 7;
        } else if (obj instanceof Integer[]) {
            return 8;
        }

        return -1;
    }

    /**
     * 统计开始到结束时间的总条数
     * @param beginT
     * @param endT
     * @param c
     * @return
     */
    public  Long count(String beginT,String endT,Class c) {
        long startT = Long.valueOf(date2TimeStamp(beginT));
        long enT = Long.valueOf(date2TimeStamp(endT));
        Query query = new Query();
        query.addCriteria(Criteria.where("recDate").gte(startT).lte(enT));
        return this.mongoTemplate.count(query, c) ;
    }

    /**
     * 统计开始时间每个小时的条数
     * @param beginT
     * @param times
     * @param c
     * @return
     */
    public  List countHous(String beginT,int times,Class c) {
        List<Object>list = new ArrayList<Object>();
        long startT = Long.valueOf(date2TimeStamp(beginT));
        long endT = Long.valueOf(startT + 3600000);
        for(int i =0;i<times;i++) {
            Query query = new Query();
            query.addCriteria(Criteria.where("recDate").gte(startT).lte(endT));
            Map<String, Object> map = new TreeMap<String, Object>(
                    new Comparator<String>() {
                        @Override
                        public int compare(String obj1, String obj2) {
                            // 降序排序
                            return obj2.compareTo(obj1);
                        }
                    });
            map.put(long2String(startT)+"--"+long2String(endT), this.mongoTemplate.count(query, c)+"条") ;
            list.add(map);
            startT = endT;
            endT = Long.valueOf(endT + 3600000);
        }
        return list ;
    }

    public static String date2TimeStamp(String date_str){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return String.valueOf(sdf.parse(date_str).getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String long2String(long time){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateString = sdf.format(new Date(time));
            return dateString;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
