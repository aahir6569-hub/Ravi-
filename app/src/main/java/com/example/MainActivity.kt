package com.example

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.ui.theme.*
import kotlinx.coroutines.delay

// High-fidelity Luxury Promotion Model
data class LuxuryPromotion(
    val id: Int,
    val title: String,
    val sponsor: String,
    val imageUrl: String,
    val promoTag: String,
    val bannerHeading: String,
    val description: String,
    val codePrefix: String,
    val deadline: String,
    val conciergePhone: String,
    val highlightColor: Color = BurnishedGold,
    val details: List<String> = emptyList()
)

// Curated selection of high-end architectural materials promotions
val PREMIUM_PROMOTIONS = listOf(
    LuxuryPromotion(
        id = 1,
        title = "TUSCAN MARBLE EMBARGO RELEASE",
        sponsor = "AUREUM MARBLE ATELIER",
        imageUrl = "https://images.unsplash.com/photo-1618221195710-dd6b41faaea6?auto=format&fit=crop&w=1200&q=80",
        promoTag = "EXCLUSIVE 20% PRE-ORDER OFF",
        bannerHeading = "MUSEUM GRADE CALACATTA",
        description = "Direct shipping container release of legendary Calacatta Gold slabs, hand-selected in Carrara, Italy. Dramatic golden veining over premium crystalline white, perfect for bespoke luxury kitchen stands, fireplace mantles, and signature master baths.",
        codePrefix = "CALA-GOLD-PRESTIGE",
        deadline = "JUNE 30, 2026",
        conciergePhone = "+18005550201",
        details = listOf(
            "Slab thickness: Standard 20mm & Extra-thick 30mm",
            "Quaried directly in the famous mountain ridges of Carrara, Italy",
            "Individually inspected and bookmatched with precision grey linings",
            "Complimentary direct crate logistics shipping and installer coordination"
        )
    ),
    LuxuryPromotion(
        id = 2,
        title = "ROYAL SMOKED WOODLAND CHROME",
        sponsor = "FOREST ROYALTY COEUR",
        imageUrl = "https://images.unsplash.com/photo-1540518614846-7eded433c457?auto=format&fit=crop&w=1200&q=80",
        promoTag = "LIMITED 15 CUSTOM RESERVATIONS",
        bannerHeading = "4-WEEK SMOKED ROYAL OAK",
        description = "Premium European Oak heartwood timber floors, deep smoked for several weeks in natural curing chambers to produce deep, rich coffee-caramel tones. Naturally stable engineered cores optimized for sophisticated interior heating matrices.",
        codePrefix = "SMOK-OAK-RESERVE",
        deadline = "JULY 15, 2026",
        conciergePhone = "+18005550202",
        details = listOf(
            "Plank width: Extra wide 220mm & lengths up to 4.2 meters",
            "Triple-cured hand-scraped micro-bevel texture",
            "FSC Eco-Certified sustainably cut ancient forest woods",
            "Optimized for modern hydronic floor warming systems"
        )
    ),
    LuxuryPromotion(
        id = 3,
        title = "MEDITERRANEAN TRAVERTINE VILLA",
        sponsor = "AUGUSTUS STONE CORP",
        imageUrl = "https://images.unsplash.com/photo-1600585154526-990dced4db0d?auto=format&fit=crop&w=1200&q=80",
        promoTag = "ZERO-INTEREST SHIPMENT RESERVES",
        bannerHeading = "HONED UNFILLED TRAVERTINE",
        description = "Sourced from the historic Roman plains of Tivoli. Natural unfilled honed travertine facades capturing warm Mediterranean sunlight with raw textural depth. Perfect for wellness pool decks, luxurious patio facades, and elegant interior walls.",
        codePrefix = "TRAV-ROME-AUGUST",
        deadline = "AUGUST 05, 2026",
        conciergePhone = "+18005550203",
        details = listOf(
            "Standard format: 600x1200mm panels",
            "Raw unfilled voids preserved for stunning visual density",
            "High frost-resistance ideal for indoor-outdoor architectural flows",
            "Custom geometric cutouts available on architect requests"
        )
    ),
    LuxuryPromotion(
        id = 4,
        title = "POLISHED BRASS ATELIER BUNDLES",
        sponsor = "METALLIC ROYALTY & CASTING",
        imageUrl = "https://images.unsplash.com/photo-1600607687939-ce8a6c25118c?auto=format&fit=crop&w=1200&q=80",
        promoTag = "COMPLIMENTARY ENGRAVING INCLUDED",
        bannerHeading = "HAND-BURNISHED SOLID BRASS",
        description = "Bespoke kitchen accents and master bathroom hardware custom ordered for elite clientele. Pure solid unlacquered brass hand-rubbed with signature micro-abrasives to guarantee a magnificent, personalized living patina.",
        codePrefix = "BRAS-AURA-ELITE",
        deadline = "JULY 31, 2026",
        conciergePhone = "+18005550204",
        details = listOf(
            "100% Solid Naval Brass construction with zero zinc fillers",
            "Unlacquered finish designed to age gracefully with touch",
            "Custom laser-embossed developer or family crest detailing",
            "Heavy commercial-grade springs and magnetic dampening latches"
        )
    )
)

data class Voucher(
    val code: String,
    val title: String,
    val discount: String,
    val sponsor: String
)

data class Booking(
    val clientName: String,
    val materialType: String,
    val consultationDate: String,
    val status: String,
    val referenceCode: String
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyApplicationTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = MatteCharcoalBlack
                ) { innerPadding ->
                    LuxuryMarketplacePromotionsScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LuxuryMarketplacePromotionsScreen(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var activeHeroIndex by remember { mutableStateOf(0) }
    var selectedPromotion by remember { mutableStateOf<LuxuryPromotion?>(null) }
    
    // Client-side persistent-like records for active interactions to keep the app highly engaging!
    val activeVouchers = remember { mutableStateListOf<Voucher>() }
    val activeBookings = remember { mutableStateListOf<Booking>() }

    var showBookingDialog by remember { mutableStateOf(false) }
    var bookingMaterialType by remember { mutableStateOf("Calacatta Marble") }

    // Auto-scroller for the stunning Top Hero Banner Carousel
    LaunchedEffect(Unit) {
        while (true) {
            delay(6000)
            activeHeroIndex = (activeHeroIndex + 1) % PREMIUM_PROMOTIONS.size
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MatteCharcoalBlack)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            
            // 1. BRAND HEADER
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "AUREUM ROYAL",
                        color = BurnishedGold,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp
                    )
                    Text(
                        text = "EXCLUSIVE MATERIAL PROMOTIONS",
                        color = PolishedPlatinum.copy(alpha = 0.6f),
                        style = MaterialTheme.typography.labelSmall,
                        letterSpacing = 1.sp
                    )
                }

                // VIP Badge trigger
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(30.dp))
                        .background(BurnishedGold.copy(alpha = 0.15f))
                        .border(1.dp, BurnishedGold, RoundedCornerShape(30.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            Icons.Default.Verified,
                            contentDescription = "VIP Status Indicator",
                            tint = ChampagneGold,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = "VIP MEMBER",
                            color = ChampagneGold,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        )
                    }
                }
            }

            // MAIN SCROLLABLE DASHBOARD
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {
                
                // 2. STUNNING TOP HERO SLIDER CAROUSEL (Homepage all banner promotion)
                Text(
                    text = "FEATURED DEALS OF THE WEEK",
                    style = MaterialTheme.typography.labelSmall,
                    color = BurnishedGold,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp,
                    modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 8.dp, bottom = 6.dp)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                        .padding(horizontal = 20.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .border(1.5.dp, BurnishedGold.copy(alpha = 0.4f), RoundedCornerShape(16.dp))
                        .clickable {
                            selectedPromotion = PREMIUM_PROMOTIONS[activeHeroIndex]
                        }
                ) {
                    val heroPromo = PREMIUM_PROMOTIONS[activeHeroIndex]

                    // Smooth transition crossfade of photos
                    Box(modifier = Modifier.fillMaxSize()) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(heroPromo.imageUrl)
                                .crossfade(true)
                                .build(),
                            contentDescription = heroPromo.title,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )

                        // Vignette bottom gradient
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.95f))
                                    )
                                )
                        )

                        // Hero Information Overlay
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.BottomStart)
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .background(BurnishedGold, RoundedCornerShape(4.dp))
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = heroPromo.promoTag,
                                    color = MatteCharcoalBlack,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 9.sp
                                )
                            }

                            Text(
                                text = heroPromo.bannerHeading,
                                color = ChampagneGold,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = heroPromo.sponsor,
                                    color = PolishedPlatinum.copy(alpha = 0.7f),
                                    style = MaterialTheme.typography.labelSmall,
                                    fontSize = 10.sp
                                )
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text(
                                        text = "VIEW CATALOG",
                                        color = BurnishedGold,
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Icon(
                                        Icons.Default.ArrowForward,
                                        contentDescription = null,
                                        tint = BurnishedGold,
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                // Custom Indicator dots underneath the sliding hero
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    PREMIUM_PROMOTIONS.forEachIndexed { idx, _ ->
                        val isSelected = activeHeroIndex == idx
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 4.dp)
                                .size(if (isSelected) 10.dp else 6.dp)
                                .clip(CircleShape)
                                .background(if (isSelected) BurnishedGold else LightSlateCharcoal)
                                .clickable { activeHeroIndex = idx }
                        )
                    }
                }

                // 3. STATS BANNER / LOYALTY PROFILE (Replaces product listings "Things" with engaging stats)
                if (activeVouchers.isNotEmpty() || activeBookings.isNotEmpty()) {
                    Text(
                        text = "YOUR ACTIVE BENEFITS",
                        style = MaterialTheme.typography.labelSmall,
                        color = BurnishedGold,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 8.dp, bottom = 4.dp)
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 6.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(WarmCharcoalOnyx)
                            .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(12.dp))
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "${activeVouchers.size}",
                                style = MaterialTheme.typography.titleMedium,
                                color = ChampagneGold,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "SAVED CODES",
                                style = MaterialTheme.typography.labelSmall,
                                color = PolishedPlatinum.copy(alpha = 0.5f)
                            )
                        }
                        Box(
                            modifier = Modifier
                                .width(1.dp)
                                .height(32.dp)
                                .background(Color.White.copy(alpha = 0.1f))
                        )
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "${activeBookings.size}",
                                style = MaterialTheme.typography.titleMedium,
                                color = ChampagneGold,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "CONSULTATIONS",
                                style = MaterialTheme.typography.labelSmall,
                                color = PolishedPlatinum.copy(alpha = 0.5f)
                            )
                        }
                        Box(
                            modifier = Modifier
                                .width(1.dp)
                                .height(32.dp)
                                .background(Color.White.copy(alpha = 0.1f))
                        )
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "GOLD CLUB",
                                style = MaterialTheme.typography.titleMedium,
                                color = BurnishedGold,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "TIER STATUS",
                                style = MaterialTheme.typography.labelSmall,
                                color = PolishedPlatinum.copy(alpha = 0.5f)
                            )
                        }
                    }
                }

                // 4. MAIN COLLATERAL CURATED BANNER FEED (Homepage all banner promotion)
                Text(
                    text = "ALL CURATED PROMOTIONS",
                    style = MaterialTheme.typography.labelSmall,
                    color = BurnishedGold,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp,
                    modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 16.dp, bottom = 12.dp)
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    PREMIUM_PROMOTIONS.forEach { promo ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedPromotion = promo }
                                .testTag("promo_banner_card_${promo.id}"),
                            colors = CardDefaults.cardColors(containerColor = WarmCharcoalOnyx),
                            shape = RoundedCornerShape(16.dp),
                            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.08f))
                        ) {
                            Column {
                                // Image Header
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(130.dp)
                                ) {
                                    AsyncImage(
                                        model = ImageRequest.Builder(LocalContext.current)
                                            .data(promo.imageUrl)
                                            .crossfade(true)
                                            .build(),
                                        contentDescription = "Promotion preview for " + promo.title,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize()
                                    )

                                    // Gradient tint overlay
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(
                                                Brush.verticalGradient(
                                                    colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f))
                                                )
                                            )
                                    )

                                    // Tag indicator overlay
                                    Box(
                                        modifier = Modifier
                                            .padding(12.dp)
                                            .background(Color.Black.copy(alpha = 0.8f), RoundedCornerShape(6.dp))
                                            .border(1.dp, BurnishedGold, RoundedCornerShape(6.dp))
                                            .padding(horizontal = 8.dp, vertical = 4.dp)
                                            .align(Alignment.TopStart)
                                    ) {
                                        Text(
                                            text = promo.promoTag,
                                            style = MaterialTheme.typography.labelSmall,
                                            color = ChampagneGold,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 9.sp
                                        )
                                    }
                                }

                                // Descriptive Core Body
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(
                                        text = promo.title,
                                        style = MaterialTheme.typography.titleMedium,
                                        color = PolishedPlatinum,
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 0.5.sp
                                    )

                                    Text(
                                        text = promo.description,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = BrushedTitanium,
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis
                                    )

                                    Divider(
                                        modifier = Modifier.padding(vertical = 8.dp),
                                        color = Color.White.copy(alpha = 0.05f)
                                    )

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column {
                                            Text(
                                                text = "SPONSOR",
                                                style = MaterialTheme.typography.labelSmall,
                                                color = PolishedPlatinum.copy(alpha = 0.4f),
                                                fontSize = 8.sp,
                                                letterSpacing = 0.5.sp
                                            )
                                            Text(
                                                text = promo.sponsor,
                                                style = MaterialTheme.typography.labelSmall,
                                                color = BurnishedGold,
                                                fontWeight = FontWeight.SemiBold
                                            )
                                        }

                                        Button(
                                            onClick = { selectedPromotion = promo },
                                            colors = ButtonDefaults.buttonColors(containerColor = BurnishedGold),
                                            shape = RoundedCornerShape(6.dp),
                                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                                        ) {
                                            Text(
                                                text = "REDEEM",
                                                color = MatteCharcoalBlack,
                                                style = MaterialTheme.typography.labelSmall,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))
            }
        }

        // 5. SCREEN MODAL OVERLAY: FULL PROMOTION SPECS & ACTION HUB
        if (selectedPromotion != null) {
            val promo = selectedPromotion!!
            var isCodeGenerated by remember(promo.id) { mutableStateOf(false) }
            var generatedCodeValue by remember(promo.id) { mutableStateOf("") }

            Dialog(
                onDismissRequest = { selectedPromotion = null }
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(horizontal = 4.dp, vertical = 16.dp),
                    shape = RoundedCornerShape(24.dp),
                    color = WarmCharcoalOnyx,
                    border = BorderStroke(1.5.dp, BurnishedGold)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState())
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Header info
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "PROMOTION DETAIL",
                                color = BurnishedGold,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                            IconButton(onClick = { selectedPromotion = null }) {
                                Icon(
                                    Icons.Default.Close,
                                    contentDescription = "Close overlay",
                                    tint = PolishedPlatinum
                                )
                            }
                        }

                        // Wide Photo Content
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp)
                                .clip(RoundedCornerShape(12.dp))
                        ) {
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(promo.imageUrl)
                                    .crossfade(true)
                                    .build(),
                                contentDescription = promo.title,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }

                        // Sponsor & Offer details
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = promo.sponsor,
                                color = BurnishedGold,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold
                            )

                            Box(
                                modifier = Modifier
                                    .background(HighContrastRed.copy(alpha = 0.15f), RoundedCornerShape(4.dp))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "ENDS ${promo.deadline}",
                                    color = HighContrastRed,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 9.sp
                                )
                            }
                        }

                        Text(
                            text = promo.title,
                            color = PolishedPlatinum,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = promo.description,
                            color = BrushedTitanium,
                            style = MaterialTheme.typography.bodyMedium
                        )

                        // Specification bullets
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.Black.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                                .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(8.dp))
                                .padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "EXCLUSIVE COMMODITY SPECS:",
                                color = ChampagneGold,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold
                            )

                            promo.details.forEach { detail ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Text(
                                        text = "•",
                                        color = BurnishedGold,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = detail,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = PolishedPlatinum.copy(alpha = 0.8f)
                                    )
                                }
                            }
                        }

                        // Dynamic Interaction: Save Coupon / Promo Code Generation
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Color.Black.copy(alpha = 0.6f)),
                            border = BorderStroke(1.dp, BurnishedGold.copy(alpha = 0.3f)),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                if (!isCodeGenerated) {
                                    Text(
                                        text = "REDEEM INSTANT DIGITAL DEBENTURE",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = PolishedPlatinum.copy(alpha = 0.6f),
                                        fontWeight = FontWeight.Bold
                                    )

                                    Button(
                                        onClick = {
                                            val randomNumber = (1000..9999).random()
                                            generatedCodeValue = "${promo.codePrefix}-$randomNumber"
                                            isCodeGenerated = true
                                            // Register voucher on our internal list
                                            activeVouchers.add(
                                                Voucher(
                                                    code = generatedCodeValue,
                                                    title = promo.title,
                                                    discount = promo.promoTag,
                                                    sponsor = promo.sponsor
                                                )
                                            )
                                            Toast.makeText(context, "Exclusive code registered directly to VIP account!", Toast.LENGTH_SHORT).show()
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = BurnishedGold),
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text(
                                            text = "GENERATE AUREUM VALUE CODE",
                                            color = MatteCharcoalBlack,
                                            style = MaterialTheme.typography.labelLarge,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                } else {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Text(
                                            text = "YOUR EXCLUSIVE VIP CODE IS SECURE:",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = SuccessEmerald,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Box(
                                            modifier = Modifier
                                                .background(BurnishedGold.copy(alpha = 0.15f), RoundedCornerShape(6.dp))
                                                .border(1.5.dp, BurnishedGold, RoundedCornerShape(6.dp))
                                                .padding(horizontal = 16.dp, vertical = 8.dp)
                                        ) {
                                            Text(
                                                text = generatedCodeValue,
                                                color = ChampagneGold,
                                                style = MaterialTheme.typography.titleMedium,
                                                fontWeight = FontWeight.Bold,
                                                letterSpacing = 1.sp
                                            )
                                        }
                                        Text(
                                            text = "Mention this security catalog code on invoice checkout",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = PolishedPlatinum.copy(alpha = 0.4f),
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }
                            }
                        }

                        // Consultation Booking Call & Direct Hotlink Dial Buttons
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Direct Voice hotlink to concierge
                            OutlinedButton(
                                onClick = {
                                    try {
                                        val phoneIntent = Intent(Intent.ACTION_DIAL).apply {
                                            data = Uri.parse("tel:${promo.conciergePhone}")
                                        }
                                        context.startActivity(phoneIntent)
                                    } catch (e: Exception) {
                                        Toast.makeText(context, "Concierge lines are busy.", Toast.LENGTH_SHORT).show()
                                    }
                                },
                                modifier = Modifier.weight(1f),
                                border = BorderStroke(1.dp, BurnishedGold),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = BurnishedGold),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Icon(Icons.Default.Phone, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    "DIAL DIRECT",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            // Private VIP Reservation booking
                            Button(
                                onClick = {
                                    bookingMaterialType = promo.title
                                    showBookingDialog = true
                                    selectedPromotion = null
                                },
                                modifier = Modifier.weight(1.2f),
                                colors = ButtonDefaults.buttonColors(containerColor = PolishedPlatinum),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Icon(Icons.Default.CalendarMonth, contentDescription = null, tint = MatteCharcoalBlack, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    "BOOK CONSULT",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MatteCharcoalBlack,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }

        // 6. SCREEN MODAL OVERLAY: CONSULTATION BOOKING DIALOG (Interactive Simulation)
        if (showBookingDialog) {
            var clientName by remember { mutableStateOf("") }
            var clientPhone by remember { mutableStateOf("+1 ") }
            var preferredDate by remember { mutableStateOf("JUNE 25, 2026") }

            Dialog(
                onDismissRequest = { showBookingDialog = false }
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(8.dp),
                    shape = RoundedCornerShape(24.dp),
                    color = WarmCharcoalOnyx,
                    border = BorderStroke(1.5.dp, BurnishedGold)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "VIP RESERVATION COURIER",
                            color = BurnishedGold,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )

                        Text(
                            text = "Register design schedule for consulting on $bookingMaterialType material. Secure luxury designer review catalog instantly.",
                            color = PolishedPlatinum.copy(alpha = 0.7f),
                            style = MaterialTheme.typography.bodySmall
                        )

                        OutlinedTextField(
                            value = clientName,
                            onValueChange = { clientName = it },
                            label = { Text("Client Full Name", color = PolishedPlatinum.copy(alpha = 0.5f)) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = BurnishedGold,
                                unfocusedBorderColor = Color.White.copy(alpha = 0.15f),
                                focusedLabelColor = BurnishedGold,
                                focusedTextColor = PolishedPlatinum,
                                unfocusedTextColor = PolishedPlatinum
                            )
                        )

                        OutlinedTextField(
                            value = clientPhone,
                            onValueChange = { clientPhone = it },
                            label = { Text("Private Telephone Number", color = PolishedPlatinum.copy(alpha = 0.5f)) },
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = BurnishedGold,
                                unfocusedBorderColor = Color.White.copy(alpha = 0.15f),
                                focusedLabelColor = BurnishedGold,
                                focusedTextColor = PolishedPlatinum,
                                unfocusedTextColor = PolishedPlatinum
                            )
                        )

                        OutlinedTextField(
                            value = preferredDate,
                            onValueChange = { preferredDate = it },
                            label = { Text("Preferred Booking Date", color = PolishedPlatinum.copy(alpha = 0.5f)) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = BurnishedGold,
                                unfocusedBorderColor = Color.White.copy(alpha = 0.15f),
                                focusedLabelColor = BurnishedGold,
                                focusedTextColor = PolishedPlatinum,
                                unfocusedTextColor = PolishedPlatinum
                            )
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TextButton(onClick = { showBookingDialog = false }) {
                                Text("CANCEL", color = PolishedPlatinum.copy(alpha = 0.6f))
                            }

                            Button(
                                onClick = {
                                    if (clientName.isNotEmpty()) {
                                        val refInt = (10000..99999).random()
                                        activeBookings.add(
                                            Booking(
                                                clientName = clientName,
                                                materialType = bookingMaterialType,
                                                consultationDate = preferredDate,
                                                status = "CONFIRMED & REGISTERED BY CONCIERGE",
                                                referenceCode = "REF-$refInt"
                                            )
                                        )
                                        showBookingDialog = false
                                        Toast.makeText(context, "Showroom private slots registered! We will reach out to you shortly.", Toast.LENGTH_LONG).show()
                                    } else {
                                        Toast.makeText(context, "Please enter your name", Toast.LENGTH_SHORT).show()
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = BurnishedGold),
                                shape = RoundedCornerShape(8.dp),
                                enabled = clientName.isNotEmpty()
                            ) {
                                Text(
                                    "CONFIRM SLOT",
                                    color = MatteCharcoalBlack,
                                    style = MaterialTheme.typography.labelLarge,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
