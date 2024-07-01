import com.comsol.model.*;
import com.comsol.model.util.*;

public class resonador_baw_chatgpt {

  public static Model run() {
    Model model = ModelUtil.create("Model");

    model.modelPath("/home/ubiedo");
    // Crea un nuevo componente en el modelo
    model.component().create("comp1", true);
    // Crea una geometría 2D
    model.component("comp1").geom().create("geom1", 2);
    // Crea una malla para el componente
    model.component("comp1").mesh().create("mesh1");
    // Crea la física del sólido
    model.component("comp1").physics().create("solid", "SolidMechanics", "geom1");
    // Crea el modelo del material piezoeléctrico con mecánica del sólido
    model.component("comp1").physics("solid").create("pzm1", "PiezoelectricMaterialModel");
    model.component("comp1").physics("solid").feature("pzm1").selection().all();
    // Crea la física electrostática
    model.component("comp1").physics().create("es", "Electrostatics", "geom1");
    // Crea el modelo del material piezoeléctrico con conservación de cargas    
    model.component("comp1").physics("es").create("ccnp1", "ChargeConservationPiezo");
    model.component("comp1").physics("es").feature("ccnp1").selection().all();
    // Crea el acoplamiento multifísico del efecto piezoeléctrico
    model.component("comp1").multiphysics().create("pze1", "PiezoelectricEffect", 2);
    model.component("comp1").multiphysics("pze1").set("Solid_physics", "solid");
    model.component("comp1").multiphysics("pze1").set("Electrostatics_physics", "es");
    // Crea el estudio para el análisis de eigenfrecuencias
    model.study().create("std1");
    model.study("std1").create("eig", "Eigenfrequency");
    model.study("std1").feature("eig").set("shift", "1[Hz]");
    model.study("std1").feature("eig").set("conrad", "1");
    model.study("std1").feature("eig").set("linpsolnum", "auto");
    model.study("std1").feature("eig").set("solnum", "auto");
    model.study("std1").feature("eig").set("notsolnum", "auto");
    model.study("std1").feature("eig").set("ngenAUX", "1");
    model.study("std1").feature("eig").set("goalngenAUX", "1");
    model.study("std1").feature("eig").set("ngenAUX", "1");
    model.study("std1").feature("eig").set("goalngenAUX", "1");
    model.study("std1").feature("eig").setSolveFor("/physics/solid", true);
    model.study("std1").feature("eig").setSolveFor("/physics/es", true);
    model.study("std1").feature("eig").setSolveFor("/multiphysics/pze1", true);
    
    model.component("comp1").common().create("mpf1", "ParticipationFactors");
    // Establece la unidad para la geometría
    model.component("comp1").geom("geom1").lengthUnit("\u00b5m");
    // Crea el primer rectángulo representando el sustrato de silicio y el piezoeléctrico de dióxido de zinc
    model.component("comp1").geom("geom1").create("r1", "Rectangle");
    model.component("comp1").geom("geom1").feature("r1").set("size", new double[]{1, 0.5});
    model.component("comp1").geom("geom1").feature("r1").set("pos", new double[]{-1.25, -0.05});
    model.component("comp1").geom("geom1").feature("r1").set("size", new double[]{1700, 16.7});
    model.component("comp1").geom("geom1").feature("r1").set("base", "center");
    model.component("comp1").geom("geom1").feature("r1").set("pos", new String[]{"0.0", "0.0"});
    model.component("comp1").geom("geom1").feature("r1").setIndex("layer", 50, 0);
    model.component("comp1").geom("geom1").feature("r1").set("layerleft", true);
    model.component("comp1").geom("geom1").feature("r1").set("layerright", true);
    model.component("comp1").geom("geom1").feature("r1").set("layerbottom", false);
    model.component("comp1").geom("geom1").run("r1");
    // Crea el segundo rectángulo representando el electrodo de masa
    model.component("comp1").geom("geom1").feature().duplicate("r2", "r1");
    model.component("comp1").geom("geom1").feature("r2").set("size", new double[]{1700, 0.2});
    model.component("comp1").geom("geom1").feature("r2").set("pos", new String[]{"0.0", "-1.25"});
    model.component("comp1").geom("geom1").run("r2");
    // Crea el tercer rectángulo representando el electrodo positivo
    model.component("comp1").geom("geom1").create("r3", "Rectangle");
    model.component("comp1").geom("geom1").feature("r3").set("size", new double[]{500, 0.2});
    model.component("comp1").geom("geom1").feature("r3").set("base", "center");
    model.component("comp1").geom("geom1").feature("r3").set("pos", new double[]{0, 8.45});
    model.component("comp1").geom("geom1").runPre("fin");
    // Configura los ajustes de visualización para la geometría
    model.component("comp1").view("view1").axis().set("xmin", -900);
    model.component("comp1").view("view1").axis().set("xmax", 900);
    model.component("comp1").view("view1").axis().set("ymin", -10);
    model.component("comp1").view("view1").axis().set("ymax", 10);
    model.component("comp1").view("view1").set("showgrid", true);
    model.component("comp1").view("view1").hideObjects().clear();
    model.component("comp1").view("view1").hideEntities().clear();
    model.component("comp1").view("view1").hideMesh().clear();
    model.component("comp1").view("view1").axis().set("viewscaletype", "automatic");
    // Ejecuta la geometría
    model.component("comp1").geom("geom1").run();
    // Define una Perfectly Matched Layer (PML) para el componente
    model.component("comp1").coordSystem().create("pml1", "PML");
    model.component("comp1").coordSystem("pml1").selection().set(1, 2, 3, 8, 9, 10);
    model.component("comp1").coordSystem("pml1").set("PMLfactor", "5");
    model.component("comp1").coordSystem("pml1").set("PMLgamma", "2");
    // Crea y configura las propiedades materiales para el silicio
    model.component("comp1").material().create("mat1", "Common");
    model.component("comp1").material("mat1").propertyGroup().create("Anisotropic", "Anisotropic");
    model.component("comp1").material("mat1").label("Si - Silicon (single-crystal, anisotropic)");
    model.component("comp1").material("mat1").set("family", "custom");
    model.component("comp1").material("mat1").set("customspecular", new double[]{0.7843137254901961, 1, 1});
    model.component("comp1").material("mat1")
         .set("customdiffuse", new double[]{0.6666666666666666, 0.6666666666666666, 0.7058823529411765});
    model.component("comp1").material("mat1")
         .set("customambient", new double[]{0.6666666666666666, 0.6666666666666666, 0.7058823529411765});
    model.component("comp1").material("mat1").set("noise", true);
    model.component("comp1").material("mat1").set("fresnel", 0.7);
    model.component("comp1").material("mat1").set("metallic", 0);
    model.component("comp1").material("mat1").set("pearl", 0);
    model.component("comp1").material("mat1").set("diffusewrap", 0);
    model.component("comp1").material("mat1").set("clearcoat", 0);
    model.component("comp1").material("mat1").set("reflectance", 0);
    model.component("comp1").material("mat1").propertyGroup("def").set("density", "2330[kg/m^3]");
    model.component("comp1").material("mat1").propertyGroup("Anisotropic")
         .set("D", new String[]{"166[GPa]", "64[GPa]", "64[GPa]", "0[GPa]", "0[GPa]", "0[GPa]", "64[GPa]", "166[GPa]", "64[GPa]", "0[GPa]", 
         "0[GPa]", "0[GPa]", "64[GPa]", "64[GPa]", "166[GPa]", "0[GPa]", "0[GPa]", "0[GPa]", "0[GPa]", "0[GPa]", 
         "0[GPa]", "80[GPa]", "0[GPa]", "0[GPa]", "0[GPa]", "0[GPa]", "0[GPa]", "0[GPa]", "80[GPa]", "0[GPa]", 
         "0[GPa]", "0[GPa]", "0[GPa]", "0[GPa]", "0[GPa]", "80[GPa]"});
    model.component("comp1").material("mat1").set("family", "custom");
    model.component("comp1").material("mat1").set("lighting", "cooktorrance");
    model.component("comp1").material("mat1").set("fresnel", 0.7);
    model.component("comp1").material("mat1").set("roughness", 0.5);
    model.component("comp1").material("mat1").set("anisotropy", 0);
    model.component("comp1").material("mat1").set("flipanisotropy", false);
    model.component("comp1").material("mat1").set("metallic", 0);
    model.component("comp1").material("mat1").set("pearl", 0);
    model.component("comp1").material("mat1").set("diffusewrap", 0);
    model.component("comp1").material("mat1").set("clearcoat", 0);
    model.component("comp1").material("mat1").set("reflectance", 0);
    model.component("comp1").material("mat1").set("ambient", "custom");
    model.component("comp1").material("mat1")
         .set("customambient", new double[]{0.6666666666666666, 0.6666666666666666, 0.7058823529411765});
    model.component("comp1").material("mat1").set("diffuse", "custom");
    model.component("comp1").material("mat1")
         .set("customdiffuse", new double[]{0.6666666666666666, 0.6666666666666666, 0.7058823529411765});
    model.component("comp1").material("mat1").set("specular", "custom");
    model.component("comp1").material("mat1").set("customspecular", new double[]{0.7843137254901961, 1, 1});
    model.component("comp1").material("mat1").set("noisecolor", "custom");
    model.component("comp1").material("mat1").set("customnoisecolor", new double[]{0, 0, 0});
    model.component("comp1").material("mat1").set("noisescale", 0);
    model.component("comp1").material("mat1").set("noise", "off");
    model.component("comp1").material("mat1").set("noisefreq", 1);
    model.component("comp1").material("mat1").set("normalnoisebrush", "0");
    model.component("comp1").material("mat1").set("normalnoisetype", "0");
    model.component("comp1").material("mat1").set("alpha", 1);
    model.component("comp1").material("mat1").set("anisotropyaxis", new double[]{0, 0, 1});
    // Crea y configura las propiedades materiales para el aluminio    
    model.component("comp1").material().create("mat2", "Common");
    model.component("comp1").material("mat2").propertyGroup().create("Enu", "Young's modulus and Poisson's ratio");
    model.component("comp1").material("mat2").label("Al - Aluminum");
    model.component("comp1").material("mat2").set("family", "aluminum");
    model.component("comp1").material("mat2").propertyGroup("def")
         .set("electricconductivity", new String[]{"35.5e6[S/m]", "0", "0", "0", "35.5e6[S/m]", "0", "0", "0", "35.5e6[S/m]"});
    model.component("comp1").material("mat2").propertyGroup("def")
         .set("thermalexpansioncoefficient", new String[]{"23.1e-6[1/K]", "0", "0", "0", "23.1e-6[1/K]", "0", "0", "0", "23.1e-6[1/K]"});
    model.component("comp1").material("mat2").propertyGroup("def").set("heatcapacity", "904[J/(kg*K)]");
    model.component("comp1").material("mat2").propertyGroup("def").set("density", "2700[kg/m^3]");
    model.component("comp1").material("mat2").propertyGroup("def")
         .set("thermalconductivity", new String[]{"237[W/(m*K)]", "0", "0", "0", "237[W/(m*K)]", "0", "0", "0", "237[W/(m*K)]"});
    model.component("comp1").material("mat2").propertyGroup("Enu").set("E", "70.0e9[Pa]");
    model.component("comp1").material("mat2").propertyGroup("Enu").set("nu", "0.35");
    model.component("comp1").material("mat2").set("family", "aluminum");
    // Crea y configura las propiedades materiales para el dióxido de zinc
    model.component("comp1").material().create("mat3", "Common");
    model.component("comp1").material("mat3").propertyGroup().create("StrainCharge", "Strain-charge form");
    model.component("comp1").material("mat3").propertyGroup().create("StressCharge", "Stress-charge form");
    model.component("comp1").material("mat3").label("Zinc Oxide");
    model.component("comp1").material("mat3").set("family", "custom");
    model.component("comp1").material("mat3").set("customspecular", new double[]{0.7843137254901961, 1, 1});
    model.component("comp1").material("mat3")
         .set("customdiffuse", new double[]{0.7843137254901961, 0.7843137254901961, 0.7843137254901961});
    model.component("comp1").material("mat3").set("noise", true);
    model.component("comp1").material("mat3").set("fresnel", 0.9);
    model.component("comp1").material("mat3").set("roughness", 0.1);
    model.component("comp1").material("mat3").set("metallic", 0);
    model.component("comp1").material("mat3").set("pearl", 0);
    model.component("comp1").material("mat3").set("diffusewrap", 0);
    model.component("comp1").material("mat3").set("clearcoat", 0);
    model.component("comp1").material("mat3").set("reflectance", 0);
    model.component("comp1").material("mat3").propertyGroup("def")
         .set("relpermittivity", new String[]{"8.5446", "0", "0", "0", "8.5446", "0", "0", "0", "10.204"});
    model.component("comp1").material("mat3").propertyGroup("def").set("density", "5680[kg/m^3]");
    model.component("comp1").material("mat3").propertyGroup("StrainCharge")
         .set("sE", new String[]{"7.86e-012[1/Pa]", "-3.43e-012[1/Pa]", "-2.21e-012[1/Pa]", "0[1/Pa]", "0[1/Pa]", "0[1/Pa]", "-3.43e-012[1/Pa]", "7.86e-012[1/Pa]", "-2.21e-012[1/Pa]", "0[1/Pa]", 
         "0[1/Pa]", "0[1/Pa]", "-2.21e-012[1/Pa]", "-2.21e-012[1/Pa]", "6.94e-012[1/Pa]", "0[1/Pa]", "0[1/Pa]", "0[1/Pa]", "0[1/Pa]", "0[1/Pa]", 
         "0[1/Pa]", "2.36e-011[1/Pa]", "0[1/Pa]", "0[1/Pa]", "0[1/Pa]", "0[1/Pa]", "0[1/Pa]", "0[1/Pa]", "2.36e-011[1/Pa]", "0[1/Pa]", 
         "0[1/Pa]", "0[1/Pa]", "0[1/Pa]", "0[1/Pa]", "0[1/Pa]", "2.26e-011[1/Pa]"});
    model.component("comp1").material("mat3").propertyGroup("StrainCharge")
         .set("dET", new String[]{"0[C/N]", "0[C/N]", "-5.43e-012[C/N]", "0[C/N]", "0[C/N]", "-5.43e-012[C/N]", "0[C/N]", "0[C/N]", "1.167e-011[C/N]", "0[C/N]", 
         "-1.134e-011[C/N]", "0[C/N]", "-1.134e-011[C/N]", "0[C/N]", "0[C/N]", "0[C/N]", "0[C/N]", "0[C/N]"});
    model.component("comp1").material("mat3").propertyGroup("StrainCharge")
         .set("epsilonrT", new String[]{"9.16", "0", "0", "0", "9.16", "0", "0", "0", "12.64"});
    model.component("comp1").material("mat3").propertyGroup("StressCharge")
         .set("cE", new String[]{"2.09714e+011[Pa]", "1.2114e+011[Pa]", "1.05359e+011[Pa]", "0[Pa]", "0[Pa]", "0[Pa]", "1.2114e+011[Pa]", "2.09714e+011[Pa]", "1.05359e+011[Pa]", "0[Pa]", 
         "0[Pa]", "0[Pa]", "1.05359e+011[Pa]", "1.05359e+011[Pa]", "2.11194e+011[Pa]", "0[Pa]", "0[Pa]", "0[Pa]", "0[Pa]", "0[Pa]", 
         "0[Pa]", "4.23729e+010[Pa]", "0[Pa]", "0[Pa]", "0[Pa]", "0[Pa]", "0[Pa]", "0[Pa]", "4.23729e+010[Pa]", "0[Pa]", 
         "0[Pa]", "0[Pa]", "0[Pa]", "0[Pa]", "0[Pa]", "4.42478e+010[Pa]"});
    model.component("comp1").material("mat3").propertyGroup("StressCharge")
         .set("eES", new String[]{"0[C/m^2]", "0[C/m^2]", "-0.567005[C/m^2]", "0[C/m^2]", "0[C/m^2]", "-0.567005[C/m^2]", "0[C/m^2]", "0[C/m^2]", "1.32044[C/m^2]", "0[C/m^2]", 
         "-0.480508[C/m^2]", "0[C/m^2]", "-0.480508[C/m^2]", "0[C/m^2]", "0[C/m^2]", "0[C/m^2]", "0[C/m^2]", "0[C/m^2]"});
    model.component("comp1").material("mat3").propertyGroup("StressCharge")
         .set("epsilonrS", new String[]{"8.5446", "0", "0", "0", "8.5446", "0", "0", "0", "10.204"});
    model.component("comp1").material("mat3").set("family", "custom");
    model.component("comp1").material("mat3").set("lighting", "cooktorrance");
    model.component("comp1").material("mat3").set("fresnel", 0.9);
    model.component("comp1").material("mat3").set("roughness", 0.1);
    model.component("comp1").material("mat3").set("anisotropy", 0);
    model.component("comp1").material("mat3").set("flipanisotropy", false);
    model.component("comp1").material("mat3").set("metallic", 0);
    model.component("comp1").material("mat3").set("pearl", 0);
    model.component("comp1").material("mat3").set("diffusewrap", 0);
    model.component("comp1").material("mat3").set("clearcoat", 0);
    model.component("comp1").material("mat3").set("reflectance", 0);
    model.component("comp1").material("mat3").set("ambient", "custom");
    model.component("comp1").material("mat3")
         .set("customambient", new double[]{0.7843137254901961, 0.7843137254901961, 0.7843137254901961});
    model.component("comp1").material("mat3").set("diffuse", "custom");
    model.component("comp1").material("mat3")
         .set("customdiffuse", new double[]{0.7843137254901961, 0.7843137254901961, 0.7843137254901961});
    model.component("comp1").material("mat3").set("specular", "custom");
    model.component("comp1").material("mat3").set("customspecular", new double[]{0.7843137254901961, 1, 1});
    model.component("comp1").material("mat3").set("noisecolor", "custom");
    model.component("comp1").material("mat3").set("customnoisecolor", new double[]{0, 0, 0});
    model.component("comp1").material("mat3").set("noisescale", 0);
    model.component("comp1").material("mat3").set("noise", "off");
    model.component("comp1").material("mat3").set("noisefreq", 1);
    model.component("comp1").material("mat3").set("normalnoisebrush", "0");
    model.component("comp1").material("mat3").set("normalnoisetype", "0");
    model.component("comp1").material("mat3").set("alpha", 1);
    model.component("comp1").material("mat3").set("anisotropyaxis", new double[]{0, 0, 1});
    // Selecciona las entidades geométricas 2, 5, 7 y 9 como aluminio
    model.component("comp1").material("mat2").selection().set(2, 5, 7, 9);
    // Selecciona las entidades geométricas 3, 6 y 10 como dióxido de zinc
    model.component("comp1").material("mat3").selection().set(3, 6, 10);
    // Establece la dimensión característica del sólido
    model.component("comp1").physics("solid").prop("d").set("d", "1.7[mm]");
    // Establece la velocidad de referencia de las ondas de compresión        
    model.component("comp1").physics("solid").prop("cref").set("cref", "9000[m/s]");
    // Selecciona las entidades geométricas 3, 6 y 10 para que sean tratadas como piezoeléctrico
    model.component("comp1").physics("solid").feature("pzm1").selection().set(3, 6, 10);
    // Crea y configura el amortiguamiento mecánico
    model.component("comp1").physics("solid").feature("pzm1").create("mdmp1", "MechanicalDamping", 2);
    model.component("comp1").physics("solid").feature("pzm1").feature("mdmp1")
         .set("DampingType", "IsotropicLossFactor");
    model.component("comp1").physics("solid").feature("pzm1").feature("mdmp1").set("eta_s_mat", "userdef");
    model.component("comp1").physics("solid").feature("pzm1").feature("mdmp1").set("eta_s", 0.001);
    // Crea y configura las pérdidas dieléctricas        
    model.component("comp1").physics("solid").feature("pzm1").create("dels1", "DielectricLoss", 2);
    model.component("comp1").physics("solid").feature("pzm1").feature("dels1").set("eta_epsilonS_mat", "userdef");
    model.component("comp1").physics("solid").feature("pzm1").feature("dels1")
         .set("eta_epsilonS", new double[]{0.01, 0, 0, 0, 0.01, 0, 0, 0, 0.01});
    // Crea y configura el modelo elástico lineal
    model.component("comp1").physics("solid").create("lemm2", "LinearElasticModel", 2);
    // Selecciona las entidades geométricas 1, 4 y 8 para el modelo elástico lineal
    model.component("comp1").physics("solid").feature("lemm2").selection().set(1, 4, 8);
    model.component("comp1").physics("solid").feature("lemm2").set("SolidModel", "Anisotropic");
    // Establece las condiciones de contorno físicas fijando las entidades geométricas 1, 3, 5, 27, 28 y 29
    model.component("comp1").physics("solid").create("fix1", "Fixed", 1);
    model.component("comp1").physics("solid").feature("fix1").selection().set(1, 3, 5, 27, 28, 29);
    // Selecciona las entidades geométricas 3, 6 y 10 para el modelo electrostático
    model.component("comp1").physics("es").selection().set(3, 6, 10);
    model.component("comp1").physics("es").prop("d").set("d", "1.7[mm]");
    // Establece las condiciones de contorno eléctricas
    // Selecciona las entidades geométricas 6, 13 y 25 para que sean tratadas como un terminal de masa
    model.component("comp1").physics("es").create("gnd1", "Ground", 1);
    model.component("comp1").physics("es").feature("gnd1").selection().set(6, 13, 25);
    // Selecciona la entidad geométrica 16 para que sea tratada como un terminal con voltaje
    model.component("comp1").physics("es").create("term1", "Terminal", 1);
    model.component("comp1").physics("es").feature("term1").selection().set(16);
    model.component("comp1").physics("es").feature("term1").set("TerminalType", "Voltage");
    // Establece los ajustes para el mallado de la geometría
    model.component("comp1").mesh("mesh1").create("map1", "Map");
    model.component("comp1").mesh("mesh1").feature("map1").create("dis1", "Distribution");
    model.component("comp1").mesh("mesh1").feature("map1").feature("dis1").selection().set(2, 21);
    model.component("comp1").mesh("mesh1").feature("map1").feature("dis1").set("numelem", 10);
    model.component("comp1").mesh("mesh1").feature("map1").create("dis2", "Distribution");
    model.component("comp1").mesh("mesh1").feature("map1").feature("dis2").selection().set(9);
    model.component("comp1").mesh("mesh1").feature("map1").feature("dis2").set("numelem", 100);
    model.component("comp1").mesh("mesh1").feature("map1").create("dis3", "Distribution");
    model.component("comp1").mesh("mesh1").feature("map1").feature("dis3").selection().set(8);
    model.component("comp1").mesh("mesh1").feature("map1").feature("dis3").set("numelem", 2);
    model.component("comp1").mesh("mesh1").feature("map1").create("dis4", "Distribution");
    model.component("comp1").mesh("mesh1").feature("map1").feature("dis4").selection().set(12);
    model.component("comp1").mesh("mesh1").feature("map1").feature("dis4").set("numelem", 3);
    model.component("comp1").mesh("mesh1").run();
    // Configura el estudio modal de los eigenvalores
    model.study("std1").feature("eig").set("eigmethod", "region");
    model.study("std1").feature("eig").set("appnreigs", 6);
    model.study("std1").feature("eig").set("eigunit", "MHz");
    model.study("std1").feature("eig").set("eigsr", 220);
    model.study("std1").feature("eig").set("eiglr", 225);
    model.study("std1").feature("eig").set("eigli", 0.1);
    model.study("std1").feature("eig").set("notlistsolnum", 1);
    model.study("std1").feature("eig").set("notsolnum", "auto");
    model.study("std1").feature("eig").set("listsolnum", 1);
    model.study("std1").feature("eig").set("solnum", "auto");
    model.study("std1").feature("eig").set("linplistsolnum", new String[]{"1"});
    model.study("std1").feature("eig").set("linpsolnum", "auto");    
    // Crea y configura la solución asociada al estudio de eigenfrecuencias
    model.sol().create("sol1");
    model.sol("sol1").study("std1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "eig");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "eig");
    model.sol("sol1").create("e1", "Eigenvalue");
    model.sol("sol1").feature("e1").set("eigref", "100");
    model.sol("sol1").feature("e1").set("eigvfunscale", "maximum");
    model.sol("sol1").feature("e1").set("eigvfunscaleparam", "1.7E-9");
    model.sol("sol1").feature("e1").set("control", "eig");
    model.sol("sol1").feature("e1").feature("aDef").set("cachepattern", true);
    model.sol("sol1").attach("std1");
    model.sol("sol1").feature("e1").set("eigref", "2e8");
    // Crea y configura el grupo de resultados y los gráficos
    model.result().create("pg1", "PlotGroup2D");
    model.result("pg1").set("data", "dset1");
    model.result("pg1").set("defaultPlotID", "modeShape");
    model.result("pg1").label("Mode Shape (solid)");
    model.result("pg1").set("showlegends", false);
    model.result("pg1").create("surf1", "Surface");
    model.result("pg1").feature("surf1").set("expr", new String[]{"solid.disp"});
    model.result("pg1").feature("surf1").set("threshold", "manual");
    model.result("pg1").feature("surf1").set("thresholdvalue", 0.2);
    model.result("pg1").feature("surf1").set("colortable", "AuroraBorealis");
    model.result("pg1").feature("surf1").create("def", "Deform");
    model.result("pg1").feature("surf1").feature("def").set("expr", new String[]{"u", "v"});
    model.result("pg1").feature("surf1").feature("def").set("descr", "Displacement field");
    // Configura las evaluaciones y tablas de resultados de las eigenfrecuencias    
    model.result().evaluationGroup().create("std1EvgFrq", "EvaluationGroup");
    model.result().evaluationGroup("std1EvgFrq").set("defaultPlotID", "eigenFrequenciesTable");
    model.result().evaluationGroup("std1EvgFrq").set("data", "dset1");
    model.result().evaluationGroup("std1EvgFrq").label("Eigenfrequencies (Study 1)");
    model.result().evaluationGroup("std1EvgFrq").create("gev1", "EvalGlobal");
    model.result().evaluationGroup("std1EvgFrq").feature("gev1").setIndex("expr", "freq*2*pi", 0);
    model.result().evaluationGroup("std1EvgFrq").feature("gev1").setIndex("unit", "rad/s", 0);
    model.result().evaluationGroup("std1EvgFrq").feature("gev1").setIndex("descr", "Angular frequency", 0);
    model.result().evaluationGroup("std1EvgFrq").feature("gev1").setIndex("expr", "imag(freq)/abs(freq)", 1);
    model.result().evaluationGroup("std1EvgFrq").feature("gev1").setIndex("unit", "1", 1);
    model.result().evaluationGroup("std1EvgFrq").feature("gev1").setIndex("descr", "Damping ratio", 1);
    model.result().evaluationGroup("std1EvgFrq").feature("gev1").setIndex("expr", "abs(freq)/imag(freq)/2", 2);
    model.result().evaluationGroup("std1EvgFrq").feature("gev1").setIndex("unit", "1", 2);
    model.result().evaluationGroup("std1EvgFrq").feature("gev1").setIndex("descr", "Quality factor", 2);
    // Configura y ejecuta estudios adicionales
    model.result().evaluationGroup().create("std1mpf1", "EvaluationGroup");
    model.result().evaluationGroup("std1mpf1").set("data", "dset1");
    model.result().evaluationGroup("std1mpf1").label("Participation Factors (Study 1)");
    model.result().evaluationGroup("std1mpf1").create("gev1", "EvalGlobal");
    model.result().evaluationGroup("std1mpf1").feature("gev1").setIndex("expr", "mpf1.pfLnormX", 0);
    model.result().evaluationGroup("std1mpf1").feature("gev1").setIndex("unit", "1", 0);
    model.result().evaluationGroup("std1mpf1").feature("gev1")
         .setIndex("descr", "Participation factor, normalized, X-translation", 0);
    model.result().evaluationGroup("std1mpf1").feature("gev1").setIndex("expr", "mpf1.pfLnormY", 1);
    model.result().evaluationGroup("std1mpf1").feature("gev1").setIndex("unit", "1", 1);
    model.result().evaluationGroup("std1mpf1").feature("gev1")
         .setIndex("descr", "Participation factor, normalized, Y-translation", 1);
    model.result().evaluationGroup("std1mpf1").feature("gev1").setIndex("expr", "mpf1.pfLnormZ", 2);
    model.result().evaluationGroup("std1mpf1").feature("gev1").setIndex("unit", "1", 2);
    model.result().evaluationGroup("std1mpf1").feature("gev1")
         .setIndex("descr", "Participation factor, normalized, Z-translation", 2);
    model.result().evaluationGroup("std1mpf1").feature("gev1").setIndex("expr", "mpf1.pfRnormX", 3);
    model.result().evaluationGroup("std1mpf1").feature("gev1").setIndex("unit", "1", 3);
    model.result().evaluationGroup("std1mpf1").feature("gev1")
         .setIndex("descr", "Participation factor, normalized, X-rotation", 3);
    model.result().evaluationGroup("std1mpf1").feature("gev1").setIndex("expr", "mpf1.pfRnormY", 4);
    model.result().evaluationGroup("std1mpf1").feature("gev1").setIndex("unit", "1", 4);
    model.result().evaluationGroup("std1mpf1").feature("gev1")
         .setIndex("descr", "Participation factor, normalized, Y-rotation", 4);
    model.result().evaluationGroup("std1mpf1").feature("gev1").setIndex("expr", "mpf1.pfRnormZ", 5);
    model.result().evaluationGroup("std1mpf1").feature("gev1").setIndex("unit", "1", 5);
    model.result().evaluationGroup("std1mpf1").feature("gev1")
         .setIndex("descr", "Participation factor, normalized, Z-rotation", 5);
    model.result().evaluationGroup("std1mpf1").feature("gev1").setIndex("expr", "mpf1.mEffLX", 6);
    model.result().evaluationGroup("std1mpf1").feature("gev1").setIndex("unit", "kg", 6);
    model.result().evaluationGroup("std1mpf1").feature("gev1")
         .setIndex("descr", "Effective modal mass, X-translation", 6);
    model.result().evaluationGroup("std1mpf1").feature("gev1").setIndex("expr", "mpf1.mEffLY", 7);
    model.result().evaluationGroup("std1mpf1").feature("gev1").setIndex("unit", "kg", 7);
    model.result().evaluationGroup("std1mpf1").feature("gev1")
         .setIndex("descr", "Effective modal mass, Y-translation", 7);
    model.result().evaluationGroup("std1mpf1").feature("gev1").setIndex("expr", "mpf1.mEffLZ", 8);

    return model;
  }

  public static Model run2(Model model) {
    model.result().evaluationGroup("std1mpf1").feature("gev1").setIndex("unit", "kg", 8);
    model.result().evaluationGroup("std1mpf1").feature("gev1")
         .setIndex("descr", "Effective modal mass, Z-translation", 8);
    model.result().evaluationGroup("std1mpf1").feature("gev1").setIndex("expr", "mpf1.mEffRX", 9);
    model.result().evaluationGroup("std1mpf1").feature("gev1").setIndex("unit", "kg*m^2", 9);
    model.result().evaluationGroup("std1mpf1").feature("gev1")
         .setIndex("descr", "Effective modal mass, X-rotation", 9);
    model.result().evaluationGroup("std1mpf1").feature("gev1").setIndex("expr", "mpf1.mEffRY", 10);
    model.result().evaluationGroup("std1mpf1").feature("gev1").setIndex("unit", "kg*m^2", 10);
    model.result().evaluationGroup("std1mpf1").feature("gev1")
         .setIndex("descr", "Effective modal mass, Y-rotation", 10);
    model.result().evaluationGroup("std1mpf1").feature("gev1").setIndex("expr", "mpf1.mEffRZ", 11);
    model.result().evaluationGroup("std1mpf1").feature("gev1").setIndex("unit", "kg*m^2", 11);
    model.result().evaluationGroup("std1mpf1").feature("gev1")
         .setIndex("descr", "Effective modal mass, Z-rotation", 11);
    // Ejecuta la solución de las eigenfrecuencias
    model.sol("sol1").runAll();
    // Ejecuta las evaluaciones
    model.result().evaluationGroup("std1EvgFrq").run();
    model.result().evaluationGroup("std1mpf1").run();
    // Establece los ajustes de visualización
    model.component("comp1").view("view1").axis().set("viewscaletype", "none");
    // Ejecuta el gráfico
    model.result("pg1").run();
    // Crea y configura el estudio de frecuencia
    model.study().create("std2");
    model.study("std2").create("freq", "Frequency");
    model.study("std2").feature("freq").setSolveFor("/physics/solid", true);
    model.study("std2").feature("freq").setSolveFor("/physics/es", true);
    model.study("std2").feature("freq").setSolveFor("/multiphysics/pze1", true);
    model.study("std2").feature("freq").set("plist", "range(220,0.1,225)[MHz]");
    // Crea y configura la solución del estudio de frecuencia
    model.sol().create("sol2");
    model.sol("sol2").study("std2");
    model.study("std2").feature("freq").set("notlistsolnum", 1);
    model.study("std2").feature("freq").set("notsolnum", "auto");
    model.study("std2").feature("freq").set("listsolnum", 1);
    model.study("std2").feature("freq").set("solnum", "auto");
    model.sol("sol2").create("st1", "StudyStep");
    model.sol("sol2").feature("st1").set("study", "std2");
    model.sol("sol2").feature("st1").set("studystep", "freq");
    model.sol("sol2").create("v1", "Variables");
    model.sol("sol2").feature("v1").set("control", "freq");
    model.sol("sol2").create("s1", "Stationary");
    model.sol("sol2").feature("s1").create("p1", "Parametric");
    model.sol("sol2").feature("s1").feature().remove("pDef");
    model.sol("sol2").feature("s1").feature("p1").set("pname", new String[]{"freq"});
    model.sol("sol2").feature("s1").feature("p1").set("plistarr", new String[]{"range(220,0.1,225)[MHz]"});
    model.sol("sol2").feature("s1").feature("p1").set("punit", new String[]{"Hz"});
    model.sol("sol2").feature("s1").feature("p1").set("pcontinuationmode", "no");
    model.sol("sol2").feature("s1").feature("p1").set("preusesol", "auto");
    model.sol("sol2").feature("s1").feature("p1").set("pdistrib", "off");
    model.sol("sol2").feature("s1").feature("p1").set("plot", "off");
    model.sol("sol2").feature("s1").feature("p1").set("plotgroup", "pg1");
    model.sol("sol2").feature("s1").feature("p1").set("probesel", "all");
    model.sol("sol2").feature("s1").feature("p1").set("probes", new String[]{});
    model.sol("sol2").feature("s1").feature("p1").set("control", "freq");
    model.sol("sol2").feature("s1").set("linpmethod", "sol");
    model.sol("sol2").feature("s1").set("linpsol", "zero");
    model.sol("sol2").feature("s1").set("control", "freq");
    model.sol("sol2").feature("s1").feature("aDef").set("cachepattern", true);
    model.sol("sol2").feature("s1").create("seDef", "Segregated");
    model.sol("sol2").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol2").feature("s1").feature("fc1").set("linsolver", "dDef");
    model.sol("sol2").feature("s1").feature().remove("fcDef");
    model.sol("sol2").feature("s1").feature().remove("seDef");
    model.sol("sol2").attach("std2");
    // Ejecuta la solución del estudio de frecuencias
    model.sol("sol2").runAll();
    // Configura y ejecuta el gráfico de estrés mecánico
    model.result().create("pg2", "PlotGroup2D");
    model.result("pg2").set("data", "dset2");
    model.result("pg2").set("defaultPlotID", "stress");
    model.result("pg2").label("Stress (solid)");
    model.result("pg2").set("frametype", "spatial");
    model.result("pg2").create("surf1", "Surface");
    model.result("pg2").feature("surf1").set("expr", new String[]{"solid.mises_peak"});
    model.result("pg2").feature("surf1").set("threshold", "manual");
    model.result("pg2").feature("surf1").set("thresholdvalue", 0.2);
    model.result("pg2").feature("surf1").set("resolution", "normal");
    model.result("pg2").feature("surf1").set("colortable", "Prism");
    model.result("pg2").feature("surf1").create("def", "Deform");
    model.result("pg2").feature("surf1").feature("def").set("expr", new String[]{"u", "v"});
    model.result("pg2").feature("surf1").feature("def").set("descr", "Displacement field");    
    model.result("pg2").label("Displacement (solid)");
    model.result("pg2").setIndex("looplevel", 16, 0);
    model.result("pg2").setIndex("looplevel", 21, 0);
    model.result("pg2").feature("surf1").set("expr", "solid.disp");
    model.result("pg2").feature("surf1").set("descr", "Displacement magnitude");
    model.result("pg2").run();
    // Configura y ejecuta el gráfico de admitancia con respecto a la frecuencia
    model.result().create("pg3", "PlotGroup1D");
    model.result("pg3").label("Admittance");
    model.result("pg3").set("data", "dset2");
    model.result("pg3").create("glob1", "Global");
    model.result("pg3").feature("glob1").set("markerpos", "datapoints");
    model.result("pg3").feature("glob1").set("linewidth", "preference");
    model.result("pg3").feature("glob1").setIndex("expr", "abs(es.Y11)", 0);
    model.result("pg3").feature("glob1").setIndex("descr", "Absolute value of admittance", 0);
    model.result("pg3").feature("glob1").set("xdataparamunit", "MHz");
    model.result("pg3").set("ylog", true);
    model.result("pg3").set("showmanualgrid", true);
    model.result("pg3").set("showxspacing", true);
    model.result("pg3").set("showyspacing", false);
    model.result("pg3").set("showsecyspacing", false);
    model.result("pg3").set("showsecyextra", false);
    model.result("pg3").run();
    // Configura y ejecuta el gráfico de factor de calidad con respecto a la frecuencia
    model.result().duplicate("pg4", "pg3");
    model.result("pg4").label("Quality Factor");
    model.result("pg4").feature("glob1").set("expr", new String[]{"solid.Q_freq"});
    model.result("pg4").feature("glob1").set("descr", new String[]{"Quality factor for frequency"});
    model.result("pg4").feature("glob1").set("unit", new String[]{"1"});
    model.result("pg4").set("ylog", false);
    model.result("pg4").set("showmanualgrid", true);
    model.result("pg4").set("showxspacing", true);
    model.result("pg4").set("showyspacing", true);
    model.result("pg4").set("showsecyspacing", false);
    model.result("pg4").set("showsecyextra", false);
    model.result("pg4").run();
    // Crea una evaluación global y una tabla para el factor de calidad
    model.result().numerical().create("gev1", "EvalGlobal");
    model.result().numerical("gev1").label("Q-Factor");
    model.result().numerical("gev1").setIndex("looplevelinput", "manual", 0);
    model.result().numerical("gev1").setIndex("looplevel", new int[]{2}, 0);
    model.result().numerical("gev1").set("expr", new String[]{"solid.Q_eig"});
    model.result().numerical("gev1").set("descr", new String[]{"Quality factor for eigenvalue"});
    model.result().numerical("gev1").set("unit", new String[]{"1"});
    model.result().table().create("tbl1", "Table");
    model.result().table("tbl1").comments("Q-Factor");
    model.result().numerical("gev1").set("table", "tbl1");
    model.result().numerical("gev1").setResult();

    model.label("baw_resonator_ai.mph");

    return model;
  }

  public static void main(String[] args) {
    Model model = run();
    run2(model);
  }

}
