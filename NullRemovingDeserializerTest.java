import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class NullRemovingDeserializerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testDeserialize_removesNullFields() throws JsonProcessingException {
        String json = "{\"itemType\":\"type1\",\"itemName\":null,\"itemQuantity\":10}";

        objectMapper.registerModule(new SimpleModule().addDeserializer(Object.class, new NullRemovingDeserializer()));

        ItemRequest itemRequest = objectMapper.readValue(json, ItemRequest.class);

        assertNotNull(itemRequest);
        assertEquals("type1", itemRequest.getItemType());
        assertNull(itemRequest.getItemName());
        assertEquals(10, itemRequest.getItemQuantity());
    }

    @Test
    public void testDeserialize_removesNestedNullFields() throws JsonProcessingException {
        String json = "{\"itemType\":\"type1\",\"itemDetails\":{\"detailName\":null,\"detailValue\":\"value1\"},\"itemQuantity\":10}";

        objectMapper.registerModule(new SimpleModule().addDeserializer(Object.class, new NullRemovingDeserializer()));

        ItemRequest itemRequest = objectMapper.readValue(json, ItemRequest.class);

        assertNotNull(itemRequest);
        assertEquals("type1", itemRequest.getItemType());
        assertNotNull(itemRequest.getItemDetails());
        assertNull(itemRequest.getItemDetails().getDetailName());
        assertEquals("value1", itemRequest.getItemDetails().getDetailValue());
        assertEquals(10, itemRequest.getItemQuantity());
    }

    @Test
    public void testDeserialize_keepsNonNullFields() throws JsonProcessingException {
        String json = "{\"itemType\":\"type1\",\"itemName\":\"itemName1\",\"itemQuantity\":10}";

        objectMapper.registerModule(new SimpleModule().addDeserializer(Object.class, new NullRemovingDeserializer()));

        ItemRequest itemRequest = objectMapper.readValue(json, ItemRequest.class);

        assertNotNull(itemRequest);
        assertEquals("type1", itemRequest.getItemType());
        assertEquals("itemName1", itemRequest.getItemName());
        assertEquals(10, itemRequest.getItemQuantity());
    }
}
