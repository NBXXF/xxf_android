package com.xxf.download.demo

import com.nbxxf.kpower.database.objectbox.entity.BaseEntity
import com.xxf.download.IDownloadEntity
import com.xxf.hash.toMurmurHash32
import com.xxf.ktx.application
import com.xxf.ktx.fileExtension
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import java.util.Date

@Entity(useNoArgConstructor = true)
class DownloadModel : BaseEntity(), IDownloadEntity {
    override var downloadUrl: String = ""
    override var downloadStatus: Long = 0
    override var downloadTotalLength: Long = -1
    override var createDate: Date = Date()
    override var updateDate: Date = Date()
    override fun getDownloadPath(): String {
        return application.cacheDir.resolve(
            downloadUrl.toMurmurHash32().toString() + "." + downloadUrl.fileExtension
        ).absolutePath
    }

    @Id(assignable = true)
    override var id: Long = 0L
        get() {
            return downloadUrl.toMurmurHash32()
        }
}