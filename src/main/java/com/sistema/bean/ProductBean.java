package com.sistema.bean;

import com.sistema.dto.ProductDTO;
import com.sistema.mapper.ProductMapper;
import com.sistema.model.Product;
import com.sistema.service.ProductService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import org.primefaces.PrimeFaces;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Named
@ViewScoped
public class ProductBean implements Serializable {

    private final ProductService productService;
    private final ProductMapper productMapper;

    private List<ProductDTO> products;

    private ProductDTO selectedProduct;

    private boolean showForm = false;

    public ProductBean(ProductService productService, ProductMapper productMapper) {
        this.productService = productService;
        this.productMapper = productMapper;
    }

    public List<ProductDTO> getProducts() { return products; }
    public void setProducts(List<ProductDTO> products) { this.products = products; }
    public ProductDTO getSelectedProduct() { return selectedProduct; }
    public void setSelectedProduct(ProductDTO selectedProduct) { this.selectedProduct = selectedProduct; }
    public boolean isShowForm() { return showForm; }
    public void setShowForm(boolean showForm) { this.showForm = showForm; }

    public void showListView() {
        this.showForm = false;
    }

    public void showNewView() {
        this.selectedProduct = new ProductDTO();
        this.selectedProduct.setStock(0);
        this.showForm = true;
    }

    @PostConstruct
    public void init() {
        loadProducts();
    }

    public void loadProducts() {
        // For simplicity in JSF without full lazy loading datatable, we load a reasonable amount.
        // Or we could just use findAllActive directly and map. Let's load top 100 for now.
        Page<Product> page = productService.findAllActive(PageRequest.of(0, 100));
        this.products = page.getContent().stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());
    }

    public void openNew() {
        this.selectedProduct = new ProductDTO();
        this.selectedProduct.setStock(0);
    }

    public void saveProduct() {
        try {
            Product entity = productMapper.toEntity(this.selectedProduct);
            
            if (this.selectedProduct.getId() == null) {
                productService.create(entity);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Producto Creado"));
            } else {
                productService.update(entity);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Producto Actualizado"));
            }

            loadProducts();
            this.showForm = false;
            PrimeFaces.current().executeScript("PF('manageProductDialog').hide()");
            PrimeFaces.current().ajax().update("form:messages", "form:dt-products", "mainPanel");
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage()));
        }
    }

    public void deleteProduct() {
        try {
            productService.deleteLogically(this.selectedProduct.getId());
            this.selectedProduct = null;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Producto Eliminado"));
            loadProducts();
            PrimeFaces.current().ajax().update("form:messages", "form:dt-products");
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage()));
        }
    }
}
