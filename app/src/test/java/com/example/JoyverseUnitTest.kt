package com.example

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.core.data.local.ActivityDao
import com.example.core.data.local.ActivityEntity
import com.example.core.data.local.JoyverseDatabase
import com.example.core.model.UserActivity
import com.example.core.model.UserProfile
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class JoyverseUnitTest {

    private lateinit var db: JoyverseDatabase
    private lateinit var activityDao: ActivityDao

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, JoyverseDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        activityDao = db.activityDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun userProfile_defaultValues_areCorrect() {
        val profile = UserProfile(
            userId = "test_user_1",
            username = "astro_dev",
            avatar = "avatar_compass"
        )
        assertEquals("test_user_1", profile.userId)
        assertEquals("astro_dev", profile.username)
        assertEquals(0, profile.xp)
        assertEquals(1, profile.level)
        assertEquals(1, profile.streak)
    }

    @Test
    fun activityEntity_domainConversion_isLossless() {
        val domain = UserActivity(
            id = "act_101",
            userId = "user_abc",
            title = "Study Japanese Kanji",
            category = "Language Learning",
            estimatedMinutes = 20,
            priority = "High",
            isCompleted = false
        )

        val entity = ActivityEntity.fromDomain(domain, isSynced = false)
        val converted = entity.toDomainModel()

        assertEquals(domain.id, converted.id)
        assertEquals(domain.userId, converted.userId)
        assertEquals(domain.title, converted.title)
        assertEquals(domain.category, converted.category)
        assertEquals(domain.estimatedMinutes, converted.estimatedMinutes)
        assertEquals(domain.priority, converted.priority)
        assertFalse(converted.isCompleted)
    }

    @Test
    fun roomDao_insertAndObserve_worksCorrectly() = runBlocking {
        val entity = ActivityEntity(
            id = "act_test_1",
            userId = "user_42",
            title = "Build Compose Animation",
            description = "Explore spring transitions",
            category = "Coding",
            icon = "lightning",
            color = "#8B5CF6",
            priority = "High",
            estimatedMinutes = 30,
            scheduledDate = "2026-11-12",
            deadline = "",
            notes = "",
            isCompleted = false,
            createdAtEpoch = System.currentTimeMillis(),
            updatedAtEpoch = System.currentTimeMillis(),
            isSynced = false
        )

        activityDao.insertOrUpdate(entity)

        val observed = activityDao.observeActivities("user_42").first()
        assertEquals(1, observed.size)
        assertEquals("Build Compose Animation", observed.first().title)

        // Toggle completion
        val updated = entity.copy(isCompleted = true)
        activityDao.insertOrUpdate(updated)

        val afterToggle = activityDao.observeActivities("user_42").first()
        assertTrue(afterToggle.first().isCompleted)
    }
}
