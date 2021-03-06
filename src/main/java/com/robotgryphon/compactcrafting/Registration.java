package com.robotgryphon.compactcrafting;

import com.robotgryphon.compactcrafting.api.layers.RecipeLayerType;
import com.robotgryphon.compactcrafting.field.block.FieldCraftingPreviewBlock;
import com.robotgryphon.compactcrafting.field.tile.FieldCraftingPreviewTile;
import com.robotgryphon.compactcrafting.items.FieldProjectorItem;
import com.robotgryphon.compactcrafting.projector.block.FieldProjectorBlock;
import com.robotgryphon.compactcrafting.projector.tile.FieldProjectorTile;
import com.robotgryphon.compactcrafting.recipes.MiniaturizationRecipe;
import com.robotgryphon.compactcrafting.recipes.MiniaturizationRecipeSerializer;
import com.robotgryphon.compactcrafting.recipes.layers.FilledComponentRecipeLayer;
import com.robotgryphon.compactcrafting.recipes.layers.HollowComponentRecipeLayer;
import com.robotgryphon.compactcrafting.recipes.layers.MixedComponentRecipeLayer;
import com.robotgryphon.compactcrafting.recipes.layers.SimpleRecipeLayerType;
import com.robotgryphon.compactcrafting.recipes.setup.BaseRecipeType;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

import static com.robotgryphon.compactcrafting.CompactCrafting.MOD_ID;

@SuppressWarnings("unchecked")
@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Registration {

    // ================================================================================================================
    //   REGISTRIES
    // ================================================================================================================


    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MOD_ID);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);
    private static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, MOD_ID);
    private static final DeferredRegister<IRecipeSerializer<?>> RECIPES = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, MOD_ID);

    public static DeferredRegister<RecipeLayerType<?>> RECIPE_LAYERS = DeferredRegister.create((Class) RecipeLayerType.class, MOD_ID);
    public static IForgeRegistry<RecipeLayerType<?>> RECIPE_LAYER_TYPES;

    static {
        RECIPE_LAYERS.makeRegistry("recipe_layers", () -> new RegistryBuilder<RecipeLayerType<?>>()
                .tagFolder("recipe_layers"));
    }

    // ================================================================================================================
    //   PROPERTIES
    // ================================================================================================================

    // ================================================================================================================
    //   BLOCKS
    // ================================================================================================================
    public static final RegistryObject<Block> FIELD_PROJECTOR_BLOCK = BLOCKS.register("field_projector", () ->
            new FieldProjectorBlock(AbstractBlock.Properties.of(Material.METAL)
                    .strength(8, 20)
            ));

    public static final RegistryObject<Block> FIELD_CRAFTING_PREVIEW_BLOCK = BLOCKS.register("field_crafting_preview", () ->
            new FieldCraftingPreviewBlock(AbstractBlock.Properties.copy(Blocks.BARRIER)));

    // ================================================================================================================
    //   ITEMS
    // ================================================================================================================
    public static final RegistryObject<Item> FIELD_PROJECTOR_ITEM = ITEMS.register("field_projector", () ->
            new FieldProjectorItem(FIELD_PROJECTOR_BLOCK.get(), new Item.Properties().tab(CompactCrafting.ITEM_GROUP)));

//    public static final RegistryObject<Item> TEST_ITEM = ITEMS.register("test", () -> new TestItem(new Item.Properties().tab(CompactCrafting.ITEM_GROUP)));

    // ================================================================================================================
    //   TILE ENTITIES
    // ================================================================================================================
    public static final RegistryObject<TileEntityType<FieldProjectorTile>> FIELD_PROJECTOR_TILE = TILE_ENTITIES.register("field_projector", () ->
            TileEntityType.Builder
                    .of(FieldProjectorTile::new, FIELD_PROJECTOR_BLOCK.get())
                    .build(null));

    public static final RegistryObject<TileEntityType<FieldCraftingPreviewTile>> FIELD_CRAFTING_PREVIEW_TILE = TILE_ENTITIES.register("field_crafting_preview", () ->
            TileEntityType.Builder
                    .of(FieldCraftingPreviewTile::new, FIELD_CRAFTING_PREVIEW_BLOCK.get())
                    .build(null));

    // ================================================================================================================
    //   MINIATURIZATION RECIPES
    // ================================================================================================================
    public static final RegistryObject<IRecipeSerializer<MiniaturizationRecipe>> MINIATURIZATION_SERIALIZER = RECIPES.register("miniaturization", MiniaturizationRecipeSerializer::new);

    public static final ResourceLocation MINIATURIZATION_RECIPE_TYPE_ID = new ResourceLocation(MOD_ID, "miniaturization_recipe");

    public static final BaseRecipeType<MiniaturizationRecipe> MINIATURIZATION_RECIPE_TYPE = new BaseRecipeType<>(MINIATURIZATION_RECIPE_TYPE_ID);

    // ================================================================================================================
    //   RECIPE LAYER SERIALIZERS
    // ================================================================================================================
    public static final RegistryObject<RecipeLayerType<FilledComponentRecipeLayer>> FILLED_LAYER_SERIALIZER =
            RECIPE_LAYERS.register("filled", () -> new SimpleRecipeLayerType<>(FilledComponentRecipeLayer.CODEC));

    public static final RegistryObject<RecipeLayerType<HollowComponentRecipeLayer>> HOLLOW_LAYER_TYPE =
            RECIPE_LAYERS.register("hollow", () -> new SimpleRecipeLayerType<>(HollowComponentRecipeLayer.CODEC));

    public static final RegistryObject<RecipeLayerType<MixedComponentRecipeLayer>> MIXED_LAYER_TYPE =
            RECIPE_LAYERS.register("mixed", () -> new SimpleRecipeLayerType<>(MixedComponentRecipeLayer.CODEC));


    public static void init() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        BLOCKS.register(eventBus);
        ITEMS.register(eventBus);
        TILE_ENTITIES.register(eventBus);
        RECIPES.register(eventBus);

        // Recipe Types (Forge Registry setup does not call this yet)
        MINIATURIZATION_RECIPE_TYPE.register();

        RECIPE_LAYERS.register(eventBus);
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public static void onRegistration(RegistryEvent.Register<RecipeLayerType<?>> evt) {
        RECIPE_LAYER_TYPES = evt.getRegistry();
    }
}
