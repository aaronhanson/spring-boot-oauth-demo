package springboot.pac4j.util

import org.springframework.jdbc.core.RowMapper

import java.sql.ResultSet
import java.sql.ResultSetMetaData
import java.sql.SQLException

class GenericRowMapper implements RowMapper {

    @Override
    Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        Map map = [:]

        ResultSetMetaData metadata = rs.getMetaData()

        (1..metadata.getColumnCount()).each { int idx ->
            map.put(toCamelCase(metadata.getColumnName(idx)), rs.getObject(idx))
        }

        return map
    }

    private String toCamelCase(String text) {
        return text.toLowerCase().replaceAll(
                "(_)([A-Za-z0-9])",
                { Object[] it -> it[2].toUpperCase() }
        )
    }

}
