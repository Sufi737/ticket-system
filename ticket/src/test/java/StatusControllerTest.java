import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.google.gson.Gson;
import com.system.ticket.entities.Status;
import com.system.ticket.rest.StatusController;
import com.system.ticket.services.StatusService;

@SpringBootTest(classes={com.system.ticket.TicketApplication.class})
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
public class StatusControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Mock
	private StatusService statusService;
	
	@InjectMocks
	private StatusController statusController;
	
	Status status = new Status(1, "open", "Open");
	
	@BeforeEach
	public void setUp() {
		//initMocks is deprecated
		MockitoAnnotations.openMocks(this);
		System.out.println("BeforeEach working!");
		this.mockMvc = MockMvcBuilders.standaloneSetup(statusController).build();
	}
	
	@Test
	public void testGetStatusByCode() throws Exception {
		Optional<Status> statusOptional = Optional.of(status);
		Mockito.when(statusService.getStatusByCode("open")).thenReturn(statusOptional);
		this.mockMvc.perform(get("/status/open")).andExpect(status().isOk());
	}
	
	@Test
	public void testCreateStatus() throws Exception {
		Gson gson = new Gson();
        String json = gson.toJson(status);
        
		Mockito.when(statusService.createStatus(status)).thenReturn(status);
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders
	            .post("/status")
	            .accept(MediaType.APPLICATION_JSON)
	            .content(json)
	            .contentType(MediaType.APPLICATION_JSON);
		
		this.mockMvc.perform(requestBuilder).andExpect(status().isOk());
	}
	
	@Test
	public void testUpdateStatus() throws Exception {
		Gson gson = new Gson();
        String json = gson.toJson(status);
        
		Mockito.when(statusService.updateStatus(status)).thenReturn(status);
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders
	            .put("/status")
	            .accept(MediaType.APPLICATION_JSON)
	            .content(json)
	            .contentType(MediaType.APPLICATION_JSON);
		
		this.mockMvc.perform(requestBuilder).andExpect(status().isOk());
	}
	
	@Test
	public void testDeleteStatus() throws Exception {
		this.mockMvc.perform(delete("/status/open")).andExpect(status().isOk());
	}
}
