package com.syb.syblibrary.util

import android.os.Environment
import android.os.StatFs

object MemoryUtil {

    /**
     * 내장 메모리 총 용량 구하기
     * @return 내장 메모리 총 용량
     */
    fun getTotalInternalMemory(): Long {
        val path = Environment.getDataDirectory()
        val stat = StatFs(path.path)
        val blockSize: Long = stat.blockSizeLong
        val totalBlocks: Long = stat.blockCountLong
        return totalBlocks * blockSize
    }

    /**
     * 이용 가능한 내장 메모리 크기 구하기
     * @return 이용 가능한 내장 메모리
     */
    fun getInternalAvaiableMemoey(): Long {
        val path = Environment.getDataDirectory()
        val stat = StatFs(path.path)
        val blockSize: Long = stat.blockSizeLong
        val avaiableBlocks: Long = stat.availableBlocksLong
        return avaiableBlocks * blockSize
    }

    /**
     * 외장 메모리 총 용량 구하기
     * @return 외장 메모리 총 용량
     */
    fun getTotalExternalMemory(): Long {
        if (isStorage(true))
        {
            val path = Environment.getExternalStorageDirectory()
            val stat = StatFs(path.path)
            val blockSize: Long = stat.blockSizeLong
            val totalBlocks: Long = stat.blockCountLong
            return totalBlocks * blockSize
        }
        return 0
    }

    /**
     * 사용 가능한 외장 메모리 크기 구하기
     * @return 사용 가능한 외장 메모리
     */
    fun getExternalAvaiableMemory(): Long
    {
        if(isStorage(true))
        {
            val path = Environment.getExternalStorageDirectory()
            val stat = StatFs(path.path)
            val blockSize: Long = stat.blockSizeLong
            val avaiableBlocks: Long = stat.availableBlocksLong
            return avaiableBlocks * blockSize
        }
        return 0
    }

    /**
     * 외장 메모리 sdcard 사용 가능한지에 대한 여부 판단
     * @return sdcard 사용 가능 여부
     */
    fun isStorage(requireWriteAccess: Boolean): Boolean {
        val state = Environment.getExternalStorageState()
        if (Environment.MEDIA_MOUNTED == state)
            return true
        else if (!requireWriteAccess && Environment.MEDIA_MOUNTED_READ_ONLY == state)
            return true
        return false

    }

    enum class MemoryUnit {
        BIT, KB, MB, GB, TB, PB, EB
    }

    /**
     * Byte 단위 변환
     * @param memory Byte 단위 값
     * @param suffix 변환할 단위 선택
     * @return 단위 변환 값
     */
    fun formatMemorySize(memory: Double, suffix: MemoryUnit): Double {
        var unitConversion = 0.0
        when (suffix) {
            MemoryUnit.BIT -> unitConversion = memory * 8
            MemoryUnit.KB -> unitConversion = (memory / 1024)
            MemoryUnit.MB -> unitConversion = (memory / (1024 * 1024))
            MemoryUnit.GB -> unitConversion = (memory / (1024 * 1024 * 1024))
            MemoryUnit.TB -> unitConversion = (memory / (1024 * 1024 * 1024 * 1024))
            MemoryUnit.PB -> unitConversion = (memory / (1024 * 1024 * 1024 * 1024 * 1024))
            MemoryUnit.EB -> unitConversion = (memory / (1024 * 1024 * 1024 * 1024 * 1024 * 1024))
        }

        return unitConversion
    }

}