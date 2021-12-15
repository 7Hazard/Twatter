// // import "reflect-metadata";
// import { Entity, Column, PrimaryColumn } from "typeorm";

import { Model, Optional } from "sequelize";

interface Attributes {
    conversationId: string
    image: string
}

// Some attributes are optional in `User.build` and `User.create` calls
interface CreationAttributes extends Optional<Attributes, "conversationId"> { }

export class Whiteboard extends Model<Attributes, CreationAttributes> implements Attributes {
    public conversationId!: string; // Note that the `null assertion` `!` is required in strict mode.
    public image!: string
}
