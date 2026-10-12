package com.example.datastoreapplication.home

/*

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.material3.OutlinedTextFieldDefaults.contentPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.datastoreapplication.R
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.datastoreapplication.Routes
import com.example.datastoreapplication.data.Item
import com.example.datastoreapplication.ui.theme.AppViewModelProviider
import kotlin.collections.emptyList
import kotlin.collections.listOf

// Color palette matching the reference screenshot
private val TopBarPurple = Color(0xFF6650A4)
private val BackgroundMint = Color(0xFFE9FBF6)
private val FabLavender = Color(0xFFE8DEF8)
private val FabIconPurple = Color(0xFF4A4459)
private val AvatarPurple = Color(0xFF3F3757)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventoryScreen(
    onAddClick: () -> Unit = {}, navController: NavHostController,
    navigateToItemUpdate: (Int) -> Unit,
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProviider .Factory)
) {
    /*
    val homeUiState by viewModel.homeUiState.collectAsState()
    var isSearching by remember { mutableStateOf(   false) }
    var searchQuery by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }

     */

    val homeUiState by viewModel.homeUiState.collectAsState()
    val selectedNames = viewModel.selectedNames
    val inSelectionMode = selectedNames.isNotEmpty()

    var isSearching by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }

    val filteredItems = remember(homeUiState.itemList, searchQuery) {
        if (searchQuery.isBlank()) homeUiState.itemList
        else homeUiState.itemList.filter {
            it.name.contains(searchQuery, ignoreCase = true)
        }
    }
    LaunchedEffect(isSearching) {
        if (isSearching) focusRequester.requestFocus()
    }
    BackHandler(enabled = inSelectionMode) { viewModel.clearSelection() }


    Scaffold(
        containerColor = BackgroundMint,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    when {
                        inSelectionMode -> Text("${selectedNames.size} selected")
                        isSearching -> TextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = {
                                Text("Search items...", color = Color.White.copy(alpha = 0.7f))
                            },
                            singleLine = true,
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                cursorColor = Color.White
                            ),
                            modifier = Modifier.focusRequester(focusRequester)
                        )
                        else -> Text(text = "Inventory", fontWeight = FontWeight.Medium)
                    }
                },
                navigationIcon = {
                    // Small circular avatar placeholder, similar to the reference
                    Box(
                        modifier = Modifier
                            .padding(start = 12.dp)
                            .size(28.dp)
                            .background(AvatarPurple, shape = RoundedCornerShape(50)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Person,
                            contentDescription = "Profile",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                },

                actions = {
                    if (inSelectionMode) {
                        IconButton(onClick = { viewModel.deleteSelected() }) {
                            Icon(Icons.Filled.Delete, contentDescription = "Delete selected")
                        }
                    } else {
                        IconButton(onClick = {
                            if (isSearching) searchQuery = ""
                            isSearching = !isSearching
                        }) {
                            Icon(
                                imageVector = if (isSearching) Icons.Filled.Close else Icons.Filled.Search,
                                contentDescription = if (isSearching) "Close search" else "Search"
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = TopBarPurple,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )
        },

        //navController.navigate(Routes.ADD_ITEM)
        floatingActionButton = {
            if (!inSelectionMode) {
                FloatingActionButton(
                    onClick = { navController.navigate(Routes.ADD_ITEM) },
                    containerColor = FabLavender,
                    contentColor = FabIconPurple,
                    shape = RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomStart = 16.dp,
                        bottomEnd = 4.dp
                    )
                ) {
                    Icon(imageVector = Icons.Filled.Add, contentDescription = "Add item")
                }
            }
        }
    ) { paddingValues ->
        EmptyInventoryContent(
            itemList = filteredItems,
            selectedNames = selectedNames,
            onItemClick = { item ->
                if (inSelectionMode) viewModel.toggleSelection(item.name)
                else navigateToItemUpdate(item.age)
            },
            onItemLongClick = { item -> viewModel.toggleSelection(item.name) },
            modifier = Modifier.padding(paddingValues)
        )
    }
}





@Composable
private fun EmptyInventoryContent(modifier: Modifier = Modifier,
                                  itemList: List<Item>,
                                  selectedNames: Set<String>,
                                  onItemClick: (Item) -> Unit,
                                  onItemLongClick: (Item) -> Unit,) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundMint),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier.padding(top = 48.dp, start = 24.dp, end = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            if (itemList.isEmpty()) {
                Text(
                    text = stringResource(R.string.no_item_description),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(20.dp),
                )
            } else {
                InventoryList(
                    itemList = itemList,
                    onItemClick = { onItemClick(it.age) },
                    contentPadding(20.dp),
                    modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.padding_small))
                )

                /*
            Text(
                text = "Oops!",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "No items in the inventory.",
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )
            Text(
                text = "Tap + to add.",
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )

             */


            }
        }

    }
    }
@Composable
 fun InventoryList(
    itemList: List<Item>,
    selectedNames: Set<String>,
    onItemClick: (Item) -> Unit,
    onItemLongClick: (Item) -> Unit,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = contentPadding
    ) {
        items(items = itemList, key = { it.name }) { item ->
            InventoryItem(
                item = item,
                modifier = Modifier
                    .padding(dimensionResource(id = R.dimen.padding_small))
                    .clickable { onItemClick(item) })
        }
    }
}



    @Composable
    fun InventoryItem(
        item: Item, modifier: Modifier = Modifier
    ) {
        Card(
            modifier = modifier,
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_large)),
                verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_small))
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = item.name,
                        style = MaterialTheme.typography.titleLarge,
                    )
                    Spacer(Modifier.weight(1f))
                    /*
                Text(
                    text = item.formatedPrice(),
                    style = MaterialTheme.typography.titleMedium
                )

                 */
                }
                Text(
                    text = stringResource(R.string.item_quantity,item.quantity.toIntOrNull() ?: 0),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }

    }



//first commit

/*
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun InventoryScreenWithItemsPreview() {

}

 */




 */



import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.datastoreapplication.R
import com.example.datastoreapplication.Routes
import com.example.datastoreapplication.data.Item
import com.example.datastoreapplication.ui.theme.AppViewModelProviider

private val TopBarPurple = Color(0xFF6650A4)
private val BackgroundMint = Color(0xFFE9FBF6)
private val FabLavender = Color(0xFFE8DEF8)
private val FabIconPurple = Color(0xFF4A4459)
private val AvatarPurple = Color(0xFF3F3757)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventoryScreen(
    onAddClick: () -> Unit = {},
    navController: NavHostController,
    navigateToItemUpdate: (Int) -> Unit,
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProviider.Factory)
) {
    val homeUiState by viewModel.homeUiState.collectAsState()
    val selectedNames = viewModel.selectedNames
    val inSelectionMode = selectedNames.isNotEmpty()

    var isSearching by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }

    val filteredItems = remember(homeUiState.itemList, searchQuery) {
        if (searchQuery.isBlank()) homeUiState.itemList
        else homeUiState.itemList.filter {
            it.name.contains(searchQuery, ignoreCase = true)
        }
    }

    LaunchedEffect(isSearching) {
        if (isSearching) focusRequester.requestFocus()
    }

    // Back button exits selection mode first
    BackHandler(enabled = inSelectionMode) { viewModel.clearSelection() }

    Scaffold(
        containerColor = BackgroundMint,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    when {
                        inSelectionMode -> Text("${selectedNames.size} selected")
                        isSearching -> TextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = {
                                Text("Search items...", color = Color.White.copy(alpha = 0.7f))
                            },
                            singleLine = true,
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                cursorColor = Color.White
                            ),
                            modifier = Modifier.focusRequester(focusRequester)
                        )
                        else -> Text(text = "Inventory", fontWeight = FontWeight.Medium)
                    }
                },
                navigationIcon = {
                    if (inSelectionMode) {
                        IconButton(onClick = { viewModel.clearSelection() }) {
                            Icon(Icons.Filled.Close, contentDescription = "Cancel selection")
                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .padding(start = 12.dp)
                                .size(28.dp)
                                .background(AvatarPurple, shape = RoundedCornerShape(50)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Person,
                                contentDescription = "Profile",
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                },
                actions = {
                    if (inSelectionMode) {
                        IconButton(onClick = { viewModel.deleteSelected() }) {
                            Icon(Icons.Filled.Delete, contentDescription = "Delete selected")
                        }
                    } else {
                        IconButton(onClick = {
                            if (isSearching) searchQuery = ""
                            isSearching = !isSearching
                        }) {
                            Icon(
                                imageVector = if (isSearching) Icons.Filled.Close else Icons.Filled.Search,
                                contentDescription = if (isSearching) "Close search" else "Search"
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = TopBarPurple,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )
        },
        floatingActionButton = {
            if (!inSelectionMode) {
                FloatingActionButton(
                    onClick = { navController.navigate(Routes.ADD_ITEM) },
                    containerColor = FabLavender,
                    contentColor = FabIconPurple,
                    shape = RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomStart = 16.dp,
                        bottomEnd = 4.dp
                    )
                ) {
                    Icon(imageVector = Icons.Filled.Add, contentDescription = "Add item")
                }
            }
        }
    ) { paddingValues ->
        EmptyInventoryContent(
            itemList = filteredItems,
            selectedNames = selectedNames,
            onItemClick = { item ->
                if (inSelectionMode) viewModel.toggleSelection(item.name)
                else navigateToItemUpdate(item.age)
            },
            onItemLongClick = { item -> viewModel.toggleSelection(item.name) },
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@Composable
private fun EmptyInventoryContent(
    modifier: Modifier = Modifier,
    itemList: List<Item>,
    selectedNames: Set<String>,
    onItemClick: (Item) -> Unit,
    onItemLongClick: (Item) -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundMint),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier.padding(top = 48.dp, start = 24.dp, end = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (itemList.isEmpty()) {
                Text(
                    text = stringResource(R.string.no_item_description),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(20.dp),
                )
            } else {
                InventoryList(
                    itemList = itemList,
                    selectedNames = selectedNames,
                    onItemClick = onItemClick,
                    onItemLongClick = onItemLongClick,
                    contentPadding = PaddingValues(20.dp),
                    modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.padding_small))
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun InventoryList(
    itemList: List<Item>,
    selectedNames: Set<String>,
    onItemClick: (Item) -> Unit,
    onItemLongClick: (Item) -> Unit,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = contentPadding
    ) {
        items(items = itemList, key = { it.name }) { item ->
            InventoryItem(
                item = item,
                selected = item.name in selectedNames,
                modifier = Modifier
                    .padding(dimensionResource(id = R.dimen.padding_small))
                    .combinedClickable(
                        onClick = { onItemClick(item) },
                        onLongClick = { onItemLongClick(item) }
                    )
            )
        }
    }
}

@Composable
fun InventoryItem(
    item: Item,
    selected: Boolean,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = if (selected) MaterialTheme.colorScheme.primaryContainer
            else MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_large)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_small))
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.titleLarge,
                )
                Spacer(Modifier.weight(1f))
            }
            Text(
                text = stringResource(R.string.item_quantity, item.quantity.toIntOrNull() ?: 0),
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}