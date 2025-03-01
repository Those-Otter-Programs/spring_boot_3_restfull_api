package com.thoseop.utils.http;

import org.springframework.stereotype.Service;

import com.thoseop.exception.InvalidSortByException;

@Service
public class HttpPaginatedUtil {

    /**
     * Method created to filter a sortBy query param from a paginated action.
     * The method expects a prefix, which is used to cover the Entity logic, then
     * it verifies the entity object properties to see if the sortBy value 
     * exists in the Entity, thus making sure it is a sortable data.
     * 
     * It throws a custom exception and avoids the PropertyReferenceException and
     * the NoSuchFieldException.
     * 
     * @param sortBy
     * @param prefix
     * @param entity
     * @return
     * @throws Exception
     */
    public static String filteringSortBy(String sortBy, String prefix, Object entity) throws Exception {
        if (sortBy.contains(prefix)) {
            sortBy = sortBy.replace(prefix, "");
            
            char c[] = sortBy.toCharArray();
//            c[0] = Character.toLowerCase(c[0]);
            c[0] += 32; // convert the first letter to lowercase by adding 32, which is the difference between uppercase and lowercase
            sortBy = new String(c);
        } else
            throw new InvalidSortByException("received sortBy is not a sortable data".formatted(sortBy));
       
        try {
            entity.getClass().getDeclaredField(sortBy);
        } catch (NoSuchFieldException ex) { 
            throw new InvalidSortByException("received sortBy is not a sortable data".formatted(sortBy));
        }
        
        return sortBy;
    }
}
