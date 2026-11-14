package com.example.feature.explore

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.designsystem.components.JoyverseCard
import com.example.ui.theme.AstralBlue
import com.example.ui.theme.CosmicBorder
import com.example.ui.theme.CosmicCard
import com.example.ui.theme.CosmicCharcoal
import com.example.ui.theme.CosmicGold
import com.example.ui.theme.CosmicNavy
import com.example.ui.theme.ElectricPurple
import com.example.ui.theme.NeonEmerald
import com.example.ui.theme.RadiantCyan
import com.example.ui.theme.StarlightMuted
import com.example.ui.theme.StarlightWhite

data class CountryDiscovery(
    val name: String,
    val flag: String,
    val capital: String,
    val continent: String,
    val language: String,
    val currency: String,
    val culturalFact: String,
    val vocabulary: String,
    val vocabularyTranslation: String
)

private val SampleCountries = listOf(
    CountryDiscovery(
        name = "Japan",
        flag = "🇯🇵",
        capital = "Tokyo",
        continent = "Asia",
        language = "Japanese",
        currency = "Japanese Yen (¥)",
        culturalFact = "Japan features ancient wooden architectural wonders that have withstood centuries of earthquakes using seismic-damping Shinbashira pillars.",
        vocabulary = "木漏れ日 (Komorebi)",
        vocabularyTranslation = "Sunlight filtering through the leaves of trees."
    ),
    CountryDiscovery(
        name = "Iceland",
        flag = "🇮🇸",
        capital = "Reykjavík",
        continent = "Europe",
        language = "Icelandic",
        currency = "Icelandic Króna (kr)",
        culturalFact = "Powered almost 100% by geothermal and hydroelectric energy. The country has a deep tradition of Jólabókaflóð (Christmas Book Flood).",
        vocabulary = "Gluggaveður",
        vocabularyTranslation = "Window-weather (weather that looks beautiful from inside but is icy outside)."
    ),
    CountryDiscovery(
        name = "Kenya",
        flag = "🇰🇪",
        capital = "Nairobi",
        continent = "Africa",
        language = "Swahili, English",
        currency = "Kenyan Shilling (KSh)",
        culturalFact = "Home to the Great Rift Valley and the Maasai Mara ecosystem, hosting the annual Great Migration of over two million animals.",
        vocabulary = "Harambee",
        vocabularyTranslation = "All pull together (a rallying cry of community solidarity and self-reliance)."
    )
)

private val SampleSpaces = listOf(
    HobbySpaceItem("Android & Kotlin", "⚡", "Technology", "Building smooth, modern Android apps with Jetpack Compose & Clean Architecture.", 482),
    HobbySpaceItem("Japanese Culture & Anime", "⛩️", "Culture", "Discussing traditions, cuisine, languages, and animated storytelling.", 619),
    HobbySpaceItem("Indie Web Builders", "🌐", "Coding", "Sharing experiments, CSS art, tools, and creative mini-applications.", 354),
    HobbySpaceItem("Astrophotography & Cosmos", "🔭", "Science", "Capturing nebulae, constellations, deep space, and planetary systems.", 287)
)

data class HobbySpaceItem(
    val title: String,
    val icon: String,
    val category: String,
    val description: String,
    val members: Int
)

@Composable
fun ExploreScreen() {
    var countryIndex by remember { mutableIntStateOf(0) }
    var searchQuery by remember { mutableStateOf("") }
    var selectedTab by remember { mutableStateOf("WorldVerse") }

    val currentCountry = SampleCountries[countryIndex % SampleCountries.size]

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CosmicNavy)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 20.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "EXPLORE UNIVERSE",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = StarlightWhite
                        )
                    )
                    Text(
                        text = "World exploration & interest-first communities",
                        style = MaterialTheme.typography.bodySmall.copy(color = RadiantCyan)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = CosmicCharcoal,
                    border = androidx.compose.foundation.BorderStroke(1.dp, CosmicBorder),
                    modifier = Modifier.clickable {
                        countryIndex = (countryIndex + 1) % SampleCountries.size
                    }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Casino,
                            contentDescription = "Random country",
                            tint = RadiantCyan,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Random",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = StarlightWhite,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Tab Selector (WorldVerse / Hobby Spaces)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(CosmicCharcoal, RoundedCornerShape(14.dp))
                    .padding(4.dp)
            ) {
                listOf("WorldVerse", "Hobby Spaces").forEach { tab ->
                    val isSelected = selectedTab == tab
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isSelected) ElectricPurple else Color.Transparent)
                            .clickable { selectedTab = tab }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = tab,
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) Color.White else StarlightMuted
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            if (selectedTab == "WorldVerse") {
                // WorldVerse Country Discovery Card
                JoyverseCard(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = CosmicCharcoal,
                    borderColor = RadiantCyan.copy(alpha = 0.5f)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = currentCountry.flag, fontSize = 36.sp)
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = currentCountry.name,
                                        style = MaterialTheme.typography.titleLarge.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = StarlightWhite
                                        )
                                    )
                                    Text(
                                        text = "${currentCountry.capital} • ${currentCountry.continent}",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = RadiantCyan
                                        )
                                    )
                                }
                            }

                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = RadiantCyan.copy(alpha = 0.15f)
                            ) {
                                Text(
                                    text = currentCountry.currency,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = RadiantCyan,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "CULTURAL HERITAGE",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = ElectricPurple,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = currentCountry.culturalFact,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = StarlightWhite,
                                lineHeight = 22.sp
                            )
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Language Word of the Day
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = CosmicCard,
                            border = androidx.compose.foundation.BorderStroke(1.dp, CosmicBorder),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Language,
                                        contentDescription = null,
                                        tint = CosmicGold,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "LANGUAGE LESSON",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = CosmicGold,
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = currentCountry.vocabulary,
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = StarlightWhite
                                    )
                                )
                                Text(
                                    text = currentCountry.vocabularyTranslation,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = StarlightMuted,
                                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                                    )
                                )
                            }
                        }
                    }
                }
            } else {
                // Hobby Spaces Directory
                SampleSpaces.forEach { space ->
                    JoyverseCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        backgroundColor = CosmicCard,
                        borderColor = CosmicBorder
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(text = space.icon, fontSize = 28.sp)
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text(
                                            text = space.title,
                                            style = MaterialTheme.typography.titleSmall.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = StarlightWhite
                                            )
                                        )
                                        Text(
                                            text = space.category,
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                color = ElectricPurple
                                            )
                                        )
                                    }
                                }

                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = ElectricPurple.copy(alpha = 0.2f)
                                ) {
                                    Text(
                                        text = "Join Space",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = RadiantCyan,
                                            fontWeight = FontWeight.Bold
                                        ),
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = space.description,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = StarlightMuted,
                                    lineHeight = 18.sp
                                )
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
