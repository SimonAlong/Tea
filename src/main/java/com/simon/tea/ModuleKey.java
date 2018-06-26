package com.simon.tea;

import static com.simon.tea.Constant.BASE_CATALOG;

import com.simon.tea.annotation.Module;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.val;

/**
 * @author zhouzhenyong
 * @since 2018/6/26 下午4:25
 */
@Accessors(chain = true)
public class ModuleKey {
    @Getter
    private String catalogStr;
    @Getter
    @Setter
    private Module module;

    public int hashCode(){
        return catalogStr.hashCode();
    }

    public boolean equals(Object object){
        if(object instanceof ModuleKey){
            return this.catalogStr.equals(ModuleKey.class.cast(object).getCatalogStr());
        }
        return false;
    }

    public ModuleKey setCatalogStr(String catalogString){
        this.catalogStr = getCmpCatalog(catalogString);
        return this;
    }

    /**
     * 获取目录的一层和二层
     * @return
     */
    private String getCmpCatalog(String catalogString){
        String cmpCatalog;
        if(catalogString.equals(BASE_CATALOG)){
            cmpCatalog = BASE_CATALOG;
        }else{
            int ind1 = catalogString.indexOf('/');
            val ind2 = catalogString.substring(1 + ind1).indexOf('/');
            if (ind2 != -1) {
                cmpCatalog = catalogString.substring(0, ind1 + 1 + ind2);
            }else{
                cmpCatalog = catalogString;
            }
        }
        return cmpCatalog;
    }
}
