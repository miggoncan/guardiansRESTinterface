package es.us.alumn.miggoncan2.model.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
@Entity
public class AllowedShift {
	@Id
	@GeneratedValue
	private Integer id;
	
	@Column(unique = true, nullable = false)
	@NotBlank
	private String shift;
	
	public AllowedShift(String shift) {
		this.shift = shift;
	}
	
	public AllowedShift() {}
}
