package com.cafe.config;
import com.cafe.model.MenuItem;
import com.cafe.model.Branch;
import com.cafe.model.Category;
import com.cafe.repository.MenuItemRepository;
import com.cafe.repository.BranchRepository;
import com.cafe.repository.BranchMenuPriceRepository;
import com.cafe.repository.CategoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSeeder {
 @Bean CommandLineRunner seedData(MenuItemRepository menuRepo, BranchRepository branchRepo,
     CategoryRepository categoryRepo){
  return args->{

   if(categoryRepo.count()==0){
    int o=0;
    categoryRepo.save(new Category("Breakfast",       o++));
    categoryRepo.save(new Category("Pizza",           o++));
    categoryRepo.save(new Category("Spaghetti",       o++));
    categoryRepo.save(new Category("Salad",           o++));
    categoryRepo.save(new Category("Soups",           o++));
    categoryRepo.save(new Category("Burgers",         o++));
    categoryRepo.save(new Category("Main Courses",    o++));
    categoryRepo.save(new Category("Combo Meals",     o++));
    categoryRepo.save(new Category("Sandwiches & Wraps", o++));
    categoryRepo.save(new Category("Cake",            o++));
    categoryRepo.save(new Category("Ice Cream",       o++));
    categoryRepo.save(new Category("Torta",           o++));
    categoryRepo.save(new Category("Cold Drinks",     o++));
    categoryRepo.save(new Category("Hot Drinks",      o++));
    categoryRepo.save(new Category("Virgin Cocktail", o++));
    categoryRepo.save(new Category("Extra",           o++));
    categoryRepo.save(new Category("Boxes",           o++));
   }

   if(branchRepo.count()==0){
    branchRepo.save(new Branch("Gelatiamo Addis Bole Bulbula","Bole Bulbula, Addis Ababa","+251 911 234 567","BOLE_BULBULA","https://maps.app.goo.gl/NE4ENNvc1zCUpEdp7"));
    branchRepo.save(new Branch("Gelatiamo Addis Bole c","Bole Bulbula93 , Addis Ababa","+251 911 345 678","BOLE_BULBULA93","https://maps.app.goo.gl/tasRkLQyknbL5UEj7"));
    branchRepo.save(new Branch("Gelatiamo Addis Summit","Summit, Addis Ababa","+251 995 280 096","SUMMIT72","https://maps.app.goo.gl/j1MmudDT5giaL24k7"));
    branchRepo.save(new Branch("Gelatiamo Addis Mekanisa","Mekanisa, Addis Ababa","+251 911 567 890","MEKANISA","https://maps.app.goo.gl/UoKbsuhbjrRHaHx59"));
    branchRepo.save(new Branch("Gelatiamo Addis Bole Medhanialem","Bole, Addis Ababa","+251 911 678 901","BOLE_MEDHANIALEM","https://maps.app.goo.gl/zfezNT4CwfF6LnM68"));
   }

   if(menuRepo.count()==0){
    menuRepo.save(new MenuItem("Pancake With Syrup / Honey","Fluffy pancakes served with syrup or honey","Breakfast",550.0,"https://brokenovenbaking.com/wp-content/uploads/2023/01/cinnamon-honey-pancakes-8.jpg",true,false));
    menuRepo.save(new MenuItem("Special Fatira","Special traditional breakfast pastry","Breakfast",550.0,"https://i.pinimg.com/736x/51/51/cd/5151cdfe886623d344d8c1c78c574425.jpg",true,false));
    menuRepo.save(new MenuItem("Chechebsa","Traditional Ethiopian breakfast","Breakfast",550.0,"https://i.pinimg.com/236x/59/eb/45/59eb45a02a8a4b20e3e15a3512d7afba.jpg",true,false));
    menuRepo.save(new MenuItem("Special Chechebsa","Special Ethiopian breakfast dish","Breakfast",600.0,"https://i.pinimg.com/1200x/c5/88/2f/c5882ff0f13a60838ab4c3805659c2d8.jpg",true,false));
    menuRepo.save(new MenuItem("Ful","Traditional fava bean dish","Breakfast",500.0,"https://i.pinimg.com/1200x/cf/6b/8f/cf6b8fae41ec06f662c9f54297f8f365.jpg",true,false));
    menuRepo.save(new MenuItem("Special Ful","Special fava bean dish with extras","Breakfast",600.0,"https://i.pinimg.com/1200x/cf/6b/8f/cf6b8fae41ec06f662c9f54297f8f365.jpg",true,false));
    menuRepo.save(new MenuItem("Avocado Toast","Fresh avocado on toast","Breakfast",500.0,"/Avocado Toast.jpg",true,false));
    menuRepo.save(new MenuItem("French Toast","Classic French toast","Breakfast",500.0,"https://images.unsplash.com/photo-1484723091739-30a097e8f929?auto=format&fit=crop&w=900&q=85",true,false));
    menuRepo.save(new MenuItem("Waffle With Syrup / Honey","Waffle served with syrup or honey","Breakfast",550.0,"https://i.pinimg.com/736x/ba/f8/3e/baf83e425cd0dcf317515063c640595c.jpg",true,false));
    menuRepo.save(new MenuItem("Waffle With Single Scoop","Waffle with single scoop ice cream","Breakfast",850.0,"https://i.pinimg.com/736x/91/50/5e/91505e67300294db12327ca5e651caca.jpg",true,false));
    menuRepo.save(new MenuItem("Waffle With Double Scoop","Waffle with double scoop ice cream","Breakfast",1150.0,"/Waffle With Double Scoop.jpg",true,true));
    menuRepo.save(new MenuItem("Waffle With Chocolate","Waffle with chocolate sauce","Breakfast",600.0,"https://i.pinimg.com/1200x/4d/cc/6f/4dcc6ffb5ebe531a8954084a94e28c63.jpg",true,false));
    menuRepo.save(new MenuItem("Brownie With Single Scoop","Chocolate brownie with single scoop","Breakfast",650.0,"https://i.pinimg.com/736x/11/7e/d7/117ed71e0aa075f34ef2dbb052a90f42.jpg",true,false));
    menuRepo.save(new MenuItem("Brownie With Double Scoop","Chocolate brownie with double scoop","Breakfast",1150.0,"https://i.pinimg.com/736x/77/22/e9/7722e966b5a62d97fc1cc02e64cb8271.jpg",true,false));
    menuRepo.save(new MenuItem("Crepes with single scoop","French crepes with single scoop ice cream","Breakfast",950.0,"https://i.pinimg.com/736x/fd/19/70/fd19702a532542a753ac4cf155df80f7.jpg",true,false));
    menuRepo.save(new MenuItem("Crepes with double scoop","French crepes with double scoop ice cream","Breakfast",1150.0,"https://i.pinimg.com/736x/0e/8c/0f/0e8c0f626cf9c49cc42cc5d5fa9e0924.jpg",true,false));
    menuRepo.save(new MenuItem("Vegetable Pizza","Fresh vegetable pizza","Pizza",900.0,"https://i.pinimg.com/736x/d4/07/93/d4079368ea58b704533faf3ad98cf183.jpg",true,false));
    menuRepo.save(new MenuItem("Margerita Pizza","Classic margerita pizza","Pizza",1100.0,"https://i.pinimg.com/736x/df/bc/51/dfbc51e79f8d81bae9ea8bb8572cea06.jpg",true,false));
    menuRepo.save(new MenuItem("Chicken Pizza","Chicken topped pizza","Pizza",1300.0,"https://i.pinimg.com/736x/86/fe/5b/86fe5b8c18ac87d25f835c8fcc5fc6e3.jpg",true,false));
    menuRepo.save(new MenuItem("Beef Pizza","Beef topped pizza","Pizza",1200.0,"https://i.pinimg.com/736x/0e/46/9f/0e469f6bf5e02d13a8c7d6e94d0cd77a.jpg",true,false));
    menuRepo.save(new MenuItem("Special Pizza","Special house pizza","Pizza",1400.0,"https://i.pinimg.com/1200x/1b/4c/ab/1b4cab813bcfabc1f9ac9aff9f54c3a1.jpg",true,false));
    menuRepo.save(new MenuItem("Salami Pizza","Salami topped pizza","Pizza",1700.0,"https://i.pinimg.com/1200x/89/f4/d2/89f4d200ee1bef09958c6cde55adcfb4.jpg",true,false));
    menuRepo.save(new MenuItem("Tuna With Cheese Pizza","Tuna and cheese pizza","Pizza",1300.0,"/Tuna With Cheese Pizza.jpg",true,false));
    menuRepo.save(new MenuItem("Tuna Pizza","Tuna topped pizza","Pizza",1100.0,"https://i.pinimg.com/1200x/99/e3/e3/99e3e3ee8df230507a876f14e7656ddf.jpg",true,false));
    menuRepo.save(new MenuItem("Spaghetti with veggie","Vegetable spaghetti","Spaghetti",650.0,"https://i.pinimg.com/1200x/00/b9/36/00b9367ade95c19569b47cea6fd51bc1.jpg",true,false));
    menuRepo.save(new MenuItem("Spaghetti with meat","Meat sauce spaghetti","Spaghetti",850.0,"https://i.pinimg.com/736x/a6/6a/03/a66a0333b456e02fd18b2bfdf6dc8529.jpg",true,false));
    menuRepo.save(new MenuItem("Spaghetti with tomato sauce","Classic tomato sauce spaghetti","Spaghetti",700.0,"/Spaghetti with tomato sauce.jpg",true,false));
    menuRepo.save(new MenuItem("Spaghetti with cream sauce","Creamy sauce spaghetti","Spaghetti",950.0,"/Spaghetti with cream sauce.jpg",true,false));
    menuRepo.save(new MenuItem("Fasting Salad","Vegan salad option","Salad",650.0,"https://i.pinimg.com/1200x/24/2e/3f/242e3fac5ed2c7479d90cf0b1d250148.jpg",true,false));
    menuRepo.save(new MenuItem("Chicken Salad","Fresh chicken salad","Salad",950.0,"https://i.pinimg.com/736x/b2/b9/5d/b2b95d9717b36b5f9e09161c766c21d7.jpg",true,false));
    menuRepo.save(new MenuItem("Tuna Salad","Fresh tuna salad","Salad",900.0,"https://i.pinimg.com/736x/23/af/b0/23afb0c47a670b9d417808d28650c399.jpg",true,false));
    menuRepo.save(new MenuItem("Chicken Burger","Grilled chicken burger","Burgers",1100.0,"https://i.pinimg.com/736x/52/7b/91/527b91e2dd095c9d29ba7d4ec98a2928.jpg",true,false));
    menuRepo.save(new MenuItem("Cheese Burger","Mini cheese burger","Burgers",950.0,"https://i.pinimg.com/736x/69/24/32/692432adf92678e5196311036c19ba10.jpg",true,false));
    menuRepo.save(new MenuItem("Beef Burger","Mini beef burger","Burgers",900.0,"/Mini Beef Burger.jpg",true,false));
    menuRepo.save(new MenuItem("Special Burger","Mini special house burger","Burgers",1150.0,"https://i.pinimg.com/736x/9b/3e/d3/9b3ed39deddba4339b0f251a776d2639.jpg",true,false));
    menuRepo.save(new MenuItem("Lasagna/Pesto","Lasagna with pesto","Main Courses",1300.0,"/Lasagna.jpg",true,true));
    menuRepo.save(new MenuItem("Tagliatelle","Fresh tagliatelle pasta","Spaghetti",950.0,"/tagliatelle2.jpg",true,false));
    menuRepo.save(new MenuItem("English Cake","Traditional English cake","Cake",160.0,"https://i.pinimg.com/1200x/3b/04/e5/3b04e58037c9a0aaae369b65b31857b3.jpg",true,false));
    menuRepo.save(new MenuItem("Banana Cake","Moist banana cake","Cake",160.0,"https://i.pinimg.com/1200x/7a/cb/73/7acb73702255fbf5c1b18ac779a86693.jpg",true,false));
    menuRepo.save(new MenuItem("Raisen Cake","Raisin cake","Cake",160.0,"https://i.pinimg.com/1200x/4a/ce/9c/4ace9cef71259ccf311311b810f90f26.jpg",true,false));
    menuRepo.save(new MenuItem("Baklava","Sweet pastry with nuts","Cake",110.0,"https://i.pinimg.com/1200x/58/4c/06/584c06663ab602926b42c7467ae41df2.jpg",true,false));
    menuRepo.save(new MenuItem("Boxegna","Traditional cake","Cake",240.0,"https://i.pinimg.com/1200x/d5/d0/af/d5d0af8e39437de63299ac0300954897.jpg",true,false));
    menuRepo.save(new MenuItem("Bombolino","Italian doughnut","Cake",130.0,"https://savoringmoments.com/wp-content/uploads/2025/06/0_1_640_N-18-e1749086032174.webp",true,false));
    menuRepo.save(new MenuItem("Donut","Fresh donut","Cake",150.0,"https://images.unsplash.com/photo-1551024601-bec78aea704b?auto=format&fit=crop&w=900&q=85",true,false));
    menuRepo.save(new MenuItem("Cheese Cake","Creamy cheesecake","Cake",350.0,"/Cheesecake.jpg",true,false));
    menuRepo.save(new MenuItem("Orange Cake","Fresh orange cake","Cake",140.0,"https://i.pinimg.com/736x/94/6d/21/946d21216aa312e4745b4901520805da.jpg",true,false));
    menuRepo.save(new MenuItem("Brownie Cake","Chocolate brownie cake","Cake",140.0,"https://i.pinimg.com/736x/f2/55/40/f2554052524946b15996540517849e67.jpg",true,false));
    menuRepo.save(new MenuItem("Chocolate Cake","Rich chocolate cake","Cake",220.0,"https://i.pinimg.com/736x/b2/ca/0a/b2ca0abdd970a84a07197c027e0df331.jpg",true,false));
    menuRepo.save(new MenuItem("Special Cake","Rich Special cake","Cake",260.0,"https://i.pinimg.com/736x/b2/ca/0a/b2ca0abdd970a84a07197c027e0df331.jpg",true,false));
    menuRepo.save(new MenuItem("White Forest","White chocolate forest cake","Cake",210.0,"https://i.pinimg.com/1200x/b4/9e/df/b49edf6b4e5616819c62fdc44ac72371.jpg",true,false));
    menuRepo.save(new MenuItem("Black Forest","Classic black forest cake","Cake",210.0,"https://i.pinimg.com/1200x/18/00/4b/18004be4e9ca2f04d7ec0271c11773d1.jpg",true,false));
    menuRepo.save(new MenuItem("Tiramisu","Classic Italian tiramisu","Cake",230.0,"/Tiramisu.jpg",true,true));
    menuRepo.save(new MenuItem("Caramel","Caramel cake","Cake",230.0,"https://i.pinimg.com/736x/20/77/da/2077da0dc94dabf14b99e27af430a5fb.jpg",true,false));
    menuRepo.save(new MenuItem("Millifoni","Layered pastry cake","Cake",240.0,"https://i.pinimg.com/1200x/36/c1/7e/36c17e4f5256c1bc4004d6bb89e3ae38.jpg",true,false));
    menuRepo.save(new MenuItem("Scoop","Single scoop of gelato","Ice Cream",300.0,"/scoop2.jpg",true,true));
    menuRepo.save(new MenuItem("Stick","Ice cream stick","Ice Cream",360.0,"/Stick2.jpg",true,true));
    menuRepo.save(new MenuItem("1Kg Ice Cream","One kilogram ice cream","Ice Cream",4800.0,"/ice cream.jpg",true,false));
    menuRepo.save(new MenuItem("1/2 Kg Ice Cream","Half kilogram ice cream","Ice Cream",2500.0,"/ice cream.jpg",true,false));
    menuRepo.save(new MenuItem("1/4 Kg Ice Cream","Half kilogram ice cream","Ice Cream",1200.0,"/ice cream.jpg",true,false));
    menuRepo.save(new MenuItem("White / Black Torta","White or black chocolate torta","Torta",2625.0,"https://i.pinimg.com/1200x/c5/41/ab/c541ab5c06e002aa8cee98a44a8e0652.jpg",true,false));
    menuRepo.save(new MenuItem("Caramel Torta","Caramel layered torta","Torta",2940.0,"https://i.pinimg.com/736x/53/c1/27/53c12783286ac0ecdcef6d2042491156.jpg",true,false));
    menuRepo.save(new MenuItem("Tiramisu Torta","Tiramisu layered torta","Torta",2940.0,"https://i.pinimg.com/736x/53/c1/27/53c12783286ac0ecdcef6d2042491156.jpg",true,false));
    menuRepo.save(new MenuItem("Special Torta","Special layered torta","Torta",3150.0,"https://i.pinimg.com/736x/53/c1/27/53c12783286ac0ecdcef6d2042491156.jpg",true,false));
    menuRepo.save(new MenuItem("Chocolate Torta","Dark chocolate torta","Torta",2940.0,"https://i.pinimg.com/736x/1b/9c/95/1b9c959a5bc1a4115e2efa789a16c6e0.jpg",true,false));
    menuRepo.save(new MenuItem("Special Chocolate Torta","Premium special chocolate torta","Torta",3675.0,"https://i.pinimg.com/736x/e4/69/0e/e4690ed2422f22a485fc9e299eba9a46.jpg",true,false));
    menuRepo.save(new MenuItem("Soft Drink","Soft drinks","Cold Drinks",140.0,"https://i.pinimg.com/736x/de/f3/ea/def3ea43fc039d9739a6d094e0da3181.jpg",true,false));
    menuRepo.save(new MenuItem("Water","Bottled water","Cold Drinks",75.0,"https://i.pinimg.com/736x/27/bb/0c/27bb0c25825e4095f69493eb6b81621a.jpg",true,false));
    menuRepo.save(new MenuItem("Iced Latte","Cold iced latte","Cold Drinks",370.0,"https://i.pinimg.com/1200x/63/7b/a2/637ba2642e27ff4a357ae66ac23ac1db.jpg",true,false));
    menuRepo.save(new MenuItem("Iced Coffee","Refreshing iced coffee","Cold Drinks",350.0,"https://i.pinimg.com/736x/c4/da/ba/c4daba7a9905f648f214d825ca298467.jpg",true,false));
    menuRepo.save(new MenuItem("Spris Juice","Fresh mixed fruit juice","Cold Drinks",380.0,"https://i.pinimg.com/736x/63/1b/a9/631ba9539c5b9a369c94bd74687842d7.jpg",true,false));
    menuRepo.save(new MenuItem("Special Juice","Special juice blend","Cold Drinks",430.0,"https://i.pinimg.com/1200x/09/eb/90/09eb905ee0d6913aedaefaea13ed7fad.jpg",true,false));
    menuRepo.save(new MenuItem("Strawberry Juice","Strawberry fruit juice","Cold Drinks",380.0,"https://i.pinimg.com/1200x/cc/4a/29/cc4a295077a12ac0c2e32e1313268bbc.jpg",true,false));
    menuRepo.save(new MenuItem("Mango Juice","Mango fruit juice","Cold Drinks",400.0,"https://i.pinimg.com/1200x/34/09/9d/34099d3b61e04f41b9aac36b873dd0d4.jpg",true,false));
    menuRepo.save(new MenuItem("Avocado Juice","Avocado fruit juice","Cold Drinks",400.0,"https://i.pinimg.com/736x/46/d8/15/46d815be729c637ceaf9be14d1f80a04.jpg",true,false));
    menuRepo.save(new MenuItem("Milk Shake","Creamy milkshake","Cold Drinks",520.0,"/Milkshake.jpg",true,true));
    menuRepo.save(new MenuItem("Strawberry Shake","Fresh strawberry shake","Cold Drinks",400.0,"https://i.pinimg.com/736x/89/0c/be/890cbe02a1349a2a25d94e5bcac32e2b.jpg",true,false));
    menuRepo.save(new MenuItem("Mango Shake","Tropical mango shake","Cold Drinks",450.0,"https://i.pinimg.com/736x/dc/f1/c6/dcf1c6d840fef506eb8b9a5ff9808850.jpg",true,false));
    menuRepo.save(new MenuItem("Avocado Shake","Creamy avocado shake","Cold Drinks",500.0,"https://i.pinimg.com/736x/ca/d6/73/cad6730874353f055279a6ed4877ef51.jpg",true,false));
    menuRepo.save(new MenuItem("Papaya Shake","Fresh papaya shake","Cold Drinks",325.0,"https://howdaily.com/wp-content/uploads/2017/07/vietnamese-papaya-smoothie.jpg",true,false));
    menuRepo.save(new MenuItem("Ambo Water","Sparkling mineral water","Cold Drinks",160.0,"https://i.pinimg.com/736x/4a/ea/5a/4aea5afe688f76f76ae71c8a9204e664.jpg",true,false));
    menuRepo.save(new MenuItem("Macchiato","Espresso with foam","Hot Drinks",180.0,"https://i.pinimg.com/736x/0f/ec/c0/0fecc0e1f33a11d2c3e2df7248bd5254.jpg",true,false));
    menuRepo.save(new MenuItem("Fasting Macchiato","Dairy-free macchiato","Hot Drinks",220.0,"https://i.pinimg.com/736x/79/be/b7/79beb78f6ccfed098556bc8b66d842e5.jpg",true,false));
    menuRepo.save(new MenuItem("Tea","Fresh tea","Hot Drinks",80.0,"https://i.pinimg.com/736x/64/fc/57/64fc57ed558bca7e7a0cfc93c4348387.jpg",true,false));
    menuRepo.save(new MenuItem("Ginger Tea","Fresh tea","Hot Drinks",120.0,"https://i.pinimg.com/736x/64/fc/57/64fc57ed558bca7e7a0cfc93c4348387.jpg",true,false));
    menuRepo.save(new MenuItem("Special Tea","Premium tea blend","Hot Drinks",230.0,"https://www.whatsoutaddis.com/wp-content/uploads/2020/07/IMG-0592.jpg",true,false));
    menuRepo.save(new MenuItem("Flavored Tea","Flavored tea selection","Hot Drinks",120.0,"https://i.pinimg.com/736x/d9/fd/ee/d9fdeea3644726045e192493bae757d8.jpg",true,false));
    menuRepo.save(new MenuItem("Coffee","Ethiopian coffee","Hot Drinks",160.0,"/Coffee.jpg",true,false));
    menuRepo.save(new MenuItem("Milk","Hot steamed milk","Hot Drinks",140.0,"https://i.pinimg.com/736x/21/5a/e4/215ae416b9554314a09d9fc0bc6344fd.jpg",true,false));
    menuRepo.save(new MenuItem("Tea With Coffee","Tea and coffee blend","Hot Drinks",180.0,"https://i.pinimg.com/736x/84/1d/91/841d91e3ac5b96db139c0e31b0070423.jpg",true,false));
    menuRepo.save(new MenuItem("Coffee Latte","Coffee latte","Hot Drinks",190.0,"https://i.pinimg.com/736x/68/46/e6/6846e666e985014c73480135b52d958e.jpg",true,false));
    menuRepo.save(new MenuItem("Cappuccino","Espresso with steamed milk","Hot Drinks",235.0,"https://i.pinimg.com/736x/f2/54/e5/f254e5c1cecdbe6480be0584f8d79c3c.jpg",true,false));
    menuRepo.save(new MenuItem("Hot Chocolate","Rich hot chocolate","Hot Drinks",300.0,"https://i.pinimg.com/236x/8f/f0/29/8ff02991a1642a4665fa1d8a62f85eff.jpg",true,false));
    menuRepo.save(new MenuItem("Espresso","Italian espresso","Hot Drinks",170.0,"https://i.pinimg.com/736x/4c/28/e2/4c28e2420bf38c50120dba0cbaf42e8d.jpg",true,false));
    menuRepo.save(new MenuItem("Americano Coffee","Espresso with water","Hot Drinks",190.0,"https://i.pinimg.com/736x/e9/89/ee/e989eeb048a4d59cf0393d4e9bf2b2b3.jpg",true,false));
    menuRepo.save(new MenuItem("Mocha","Traditional coffee drink","Hot Drinks",180.0,"https://i.pinimg.com/1200x/45/9a/fb/459afbc3f9000c8001ab06e67549b924.jpg",true,false));
    menuRepo.save(new MenuItem("Caramel Macchiato","Caramel espresso drink","Hot Drinks",240.0,"https://i.pinimg.com/1200x/3c/a0/e6/3ca0e62d6ede348d8943868d21c36a9c.jpg",true,false));
    menuRepo.save(new MenuItem("Affogato","Espresso over ice cream","Hot Drinks",500.0,"https://i.pinimg.com/236x/4e/12/0e/4e120edff403ca2df7731721688ddfac.jpg",true,false));
    menuRepo.save(new MenuItem("Classic Mojito","Classic virgin mojito","Virgin Cocktail",300.0,"https://i.pinimg.com/736x/92/96/f1/9296f1c883062084feb677b98274fc0e.jpg",true,false));
    menuRepo.save(new MenuItem("Mango Mojito","Mango flavored mojito","Virgin Cocktail",350.0,"https://i.pinimg.com/236x/d1/34/af/d134af1cb8552ef8312be6790c67f2a1.jpg",true,false));
    menuRepo.save(new MenuItem("Strawberry Mojito","Strawberry flavored mojito","Virgin Cocktail",350.0,"/Strawberry Mojito.jpg",true,true));
    menuRepo.save(new MenuItem("Blue Hawaiian Mojito","Blue Hawaiian flavored mojito","Virgin Cocktail",350.0,"https://i.pinimg.com/736x/8f/ec/5a/8fec5a95b636239e42c074aff0c2fea3.jpg",true,false));
    menuRepo.save(new MenuItem("Gelatino Special Mojito","Special house mojito","Virgin Cocktail",400.0,"https://en.cocktail.fabbri1905.com/imgpub/2037280/600/0/fp_fdl_menta.webp",true,false));
    menuRepo.save(new MenuItem("Virgin Espresso Mojito","Coffee flavored mojito","Virgin Cocktail",350.0,"https://i.pinimg.com/736x/53/27/d3/5327d3b6d15b1182b6ea5ac2c16fcced.jpg",true,false));
    menuRepo.save(new MenuItem("Virgin Margarita Mojito","Margarita style mojito","Virgin Cocktail",350.0,"https://i.pinimg.com/736x/df/db/e4/dfdbe4f347971c75b7dd3fe36c0ba40e.jpg",true,false));
    menuRepo.save(new MenuItem("Veggie Wrap","Fresh vegetable wrap","Sandwiches & Wraps",700.0,"https://i.pinimg.com/736x/28/04/93/280493a88e7962607878b5136a9e2768.jpg",true,false));
    menuRepo.save(new MenuItem("Chicken Wrap","Grilled chicken wrap","Sandwiches & Wraps",970.0,"https://i.pinimg.com/736x/9b/44/99/9b449935462de2ef94bf109e5b9d79e0.jpg",true,true));
    menuRepo.save(new MenuItem("Egg Sandwich","Fresh egg sandwich","Sandwiches & Wraps",530.0,"https://i.pinimg.com/1200x/3e/56/bb/3e56bbc888cb325ab3b4a4e1042254e5.jpg",true,false));
    menuRepo.save(new MenuItem("Special Egg Sandwich","Special egg sandwich with extras","Sandwiches & Wraps",680.0,"https://i.pinimg.com/736x/04/bf/1f/04bf1f7ce96caa74e704aa3e80d693c0.jpg",true,false));
    menuRepo.save(new MenuItem("Club Sandwich","Classic club sandwich","Sandwiches & Wraps",1050.0,"/Club Sandwich.jpg",true,false));
    menuRepo.save(new MenuItem("Special Wrap","Special house wrap","Sandwiches & Wraps",1300.0,"https://i.pinimg.com/736x/60/40/84/6040845f460793694760683d48b28f6e.jpg",true,false));
    menuRepo.save(new MenuItem("Tuna Wrap","Tuna wrap","Sandwiches & Wraps",850.0,"https://i.pinimg.com/236x/bc/62/13/bc62136869801a5115a7452c84b98f97.jpg",true,false));
    menuRepo.save(new MenuItem("Tuna sandwich","Fresh tuna sandwich","Sandwiches & Wraps",780.0,"https://i.pinimg.com/1200x/45/3d/81/453d812e4dfbb51c054d307e6b9165f1.jpg",true,false));
    menuRepo.save(new MenuItem("Mayonnaise","Extra mayonnaise","Extra",100.0,"https://i.pinimg.com/1200x/f5/9e/0f/f59e0fd04b8cfe2e7721b6480dfcaff4.jpg",true,false));
    menuRepo.save(new MenuItem("Egg","Extra egg","Extra",65.0,"https://i.pinimg.com/736x/95/06/cf/9506cfe34ed569ddb2650c79c2b97744.jpg",true,false));
    menuRepo.save(new MenuItem("Honey","Extra honey","Extra",65.0,"https://i.pinimg.com/1200x/65/39/ad/6539ad611735f169d01c2f7dea941893.jpg",true,false));
    menuRepo.save(new MenuItem("Tuna","Extra tuna","Extra",350.0,"https://i.pinimg.com/1200x/b0/67/f7/b067f7507e4537a6420aa6d6c3a4ed29.jpg",true,false));
    menuRepo.save(new MenuItem("Cheese","Extra cheese","Extra",200.0,"https://i.pinimg.com/736x/c4/7a/73/c47a734bd8ed0c1073702df9d454117c.jpg",true,false));
    menuRepo.save(new MenuItem("French fries","Side of french fries","Extra",450.0,"https://i.pinimg.com/736x/23/9a/cc/239accb1ea2e0d12e41a376a9adfd94b.jpg",true,false));
    menuRepo.save(new MenuItem("Takeaway Box 500g","Takeaway box 500 grams","Boxes",130.0,"https://i.pinimg.com/736x/90/bf/e8/90bfe81508ec5a627fe72179940fd640.jpg",true,false));
    menuRepo.save(new MenuItem("Takeaway Box 750g","Takeaway box 750 grams","Boxes",150.0,"https://i.pinimg.com/1200x/3b/e9/2f/3be92f71cf6efd57e2c84e1879bb4753.jpg",true,false));
    menuRepo.save(new MenuItem("Takeaway Box 1kg","Takeaway box 1 kilogram","Boxes",200.0,"https://i.pinimg.com/736x/60/1e/cf/601ecf1412343f4d9a05c57ed69a51da.jpg",true,false));
    menuRepo.save(new MenuItem("Takeaway Alu 500g","Aluminum takeaway box 500 grams","Boxes",100.0,"https://i.pinimg.com/736x/a1/94/66/a194660ba19fa2c923651bc6efe57c72.jpg",true,false));
    menuRepo.save(new MenuItem("Takeaway Alu 1kg","Aluminum takeaway box 1 kilogram","Boxes",150.0,"https://i.pinimg.com/1200x/5d/9b/ba/5d9bba1f8aa83a36015687bd318ce1a7.jpg",true,false));
    menuRepo.save(new MenuItem("Pizza Takeaway Box","Pizza Takeaway Box","Boxes",150.0,"https://i.pinimg.com/1200x/5d/9b/ba/5d9bba1f8aa83a36015687bd318ce1a7.jpg",true,false));
    menuRepo.save(new MenuItem("Takeaway cup","Takeaway cup","Boxes",30.0,"https://i.pinimg.com/1200x/5d/9b/ba/5d9bba1f8aa83a36015687bd318ce1a7.jpg",true,false));
   }

   // Branch menus start empty. Admins assign items to each branch individually
   // via the admin panel (Branches → Menu → Add Items).
  };
 }
}
