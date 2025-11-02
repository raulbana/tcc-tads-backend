package br.ufpr.tads.daily_iu_services.adapter.output.content

import br.ufpr.tads.daily_iu_services.domain.entity.content.Content
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.PageImpl
import org.springframework.stereotype.Repository
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import jakarta.persistence.ParameterMode

// Implementação do repositório custom que chama a stored procedure sp_GetRecommendedContents
@Repository
class ContentRepositoryImpl : ContentRepositoryCustom {

    @PersistenceContext
    private lateinit var em: EntityManager

    override fun findRecommendedByUserLikes(userId: Long, pageable: Pageable): Page<Content> {

        val sp = em.createStoredProcedureQuery("sp_GetRecommendedContents")
        sp.registerStoredProcedureParameter("UserId", java.lang.Long::class.java, ParameterMode.IN)
        sp.registerStoredProcedureParameter("PageNumber", Integer::class.java, ParameterMode.IN)
        sp.registerStoredProcedureParameter("PageSize", Integer::class.java, ParameterMode.IN)
        sp.registerStoredProcedureParameter("TotalCount", java.lang.Long::class.java, ParameterMode.OUT)

        sp.setParameter("UserId", userId)
        sp.setParameter("PageNumber", pageable.pageNumber + 1) // SP usa paginação 1-based
        sp.setParameter("PageSize", pageable.pageSize)

        sp.execute()

        @Suppress("UNCHECKED_CAST")
        val rows = sp.resultList as List<Any>

        // Extrai ids do primeiro campo de cada linha retornada pela SP
        val ids = rows.mapNotNull { row ->
            when (row) {
                is Array<*> -> (row.getOrNull(0) as? Number)?.toLong()
                is Map<*, *> -> (row["id"] as? Number)?.toLong()
                is Number -> row.toLong()
                else -> null
            }
        }

        if (ids.isEmpty()) {
            val totalObj = try { sp.getOutputParameterValue("TotalCount") } catch (_: Exception) { null }
            val total = when (totalObj) {
                is Number -> totalObj.toLong()
                is String -> totalObj.toLongOrNull() ?: 0L
                else -> 0L
            }
            return PageImpl(emptyList(), pageable, total)
        }

        // Buscar entidades Content em lote preservando a ordem retornada pela SP
        val idsCsv = ids.joinToString(",")
        val caseOrdering = ids.mapIndexed { index, id -> "WHEN $id THEN $index" }.joinToString(" ")
        val sql = "SELECT * FROM content WHERE id IN ($idsCsv) ORDER BY CASE id $caseOrdering END"

        @Suppress("UNCHECKED_CAST")
        val contents = em.createNativeQuery(sql, Content::class.java).resultList as List<Content>

        val totalObj = try { sp.getOutputParameterValue("TotalCount") } catch (_: Exception) { null }
        val total = when (totalObj) {
            is Number -> totalObj.toLong()
            is String -> totalObj.toLongOrNull() ?: contents.size.toLong()
            else -> contents.size.toLong()
        }

        return PageImpl(contents, pageable, total)
    }
}