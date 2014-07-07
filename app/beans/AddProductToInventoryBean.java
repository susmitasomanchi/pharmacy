package beans;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import models.AppUser;
import models.Product;
import models.pharmacist.Batch;
import models.pharmacist.Inventory;
import play.data.validation.Constraints.Required;

@SuppressWarnings("serial")
public class AddProductToInventoryBean implements Serializable{

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Long id;


	public String batchNo;


	public Double mrp;


	public Date expiryDate;


	public Integer quantity;

	public Float tax;

	public Float discount;


	public String shelfNo;

	public Integer productQuantity;

	public String remarks;

	public Long product;

	public Batch toBatchEntity(){

		final Batch batch = new Batch();

		batch.id=this.id;


		if(this.batchNo !=null)
			batch.batchNo=this.batchNo;

		if(this.mrp !=null)
			batch.mrp=this.mrp;

		if(this.expiryDate !=null)
			batch.expiryDate=this.expiryDate;

		if(this.quantity !=null)
			batch.quantity=this.quantity;

		if(this.tax !=null)
			batch.tax=this.tax;

		if(this.discount !=null)
			batch.discount=this.discount;

		if(this.product !=null)
			batch.product=Product.find.byId(this.product);

		return batch;

	}

	public Inventory toInventoryEntity(){

		final Inventory inventory=new Inventory();

		inventory.id=this.id;

		if(this.shelfNo !=null)
			inventory.shelfNo=this.shelfNo;

		if(this.productQuantity !=null)
			inventory.productQuantity=this.productQuantity;

		if(this.remarks !=null)
			inventory.remarks=this.remarks;

		if(this.product !=null)
			inventory.product=Product.find.byId(this.product);


		return inventory;

	}

}
