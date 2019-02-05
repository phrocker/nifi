package org.apache.nifi.processor;

import org.apache.nifi.components.PropertyDescriptor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JniComponent {
    private String type;
    private List<PropertyDescriptor> descriptorsList;
    private boolean dynamicProperties;
    private boolean dynamicRelationships;

    private JniComponent(final String type){
        this.type = type;
        this.descriptorsList = new ArrayList<>();
        this.dynamicProperties = false;
        this.dynamicRelationships = false;
    }

    private JniComponent(final String type,final List<PropertyDescriptor> descriptorsList, final boolean dynamicProperties, final boolean dynamicRelationships){
        this.type = type;
        this.descriptorsList = new ArrayList<>(descriptorsList);
        this.dynamicProperties = dynamicProperties;
        this.dynamicRelationships = dynamicRelationships;
    }

    public String getType(){
        return type;
    }

    public List<PropertyDescriptor> getDescriptors(){
        return Collections.unmodifiableList(descriptorsList);
    }

    public boolean getDynamicRelationshipsSupported(){
        return dynamicRelationships;
    }

    public boolean getDynamicPropertiesSupported(){
        return dynamicProperties;
    }

    public static class JniComponentBuilder{

        public static JniComponentBuilder create(final String type){
            return new JniComponentBuilder(type);
        }


        public JniComponentBuilder addProperty(final PropertyDescriptor property){
            component.descriptorsList.add(property);
            return this;
        }

        public JniComponentBuilder addProperties(final List<PropertyDescriptor> descriptorsList){
            component.descriptorsList.addAll(descriptorsList);
            return this;
        }

        public JniComponentBuilder setDynamicProperties(){
            component.dynamicProperties = true;
            return this;
        }

        public JniComponentBuilder setDynamicRelationships(){
            component.dynamicRelationships = true;
            return this;
        }

        public JniComponent build() {
            return component;
        }



       private JniComponentBuilder(final String type){
        component = new JniComponent(type);
        }

        private JniComponent component;

    }


}
