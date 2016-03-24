package com.benoitletondor.materiallist.stub;

import java.util.Date;

/**
 * Helper that creates stub media objects
 *
 * @author Benoit LETONDOR
 */
public class StubMedias
{
    public static Media media1()
    {
        return new Media(1,
            Media.MediaType.IMAGE,
            "https://c2.staticflickr.com/2/1442/25963364396_ce8de12718_h.jpg",
            "https://c2.staticflickr.com/2/1442/25963364396_89e83fde40_z.jpg",
            "The embarcadero",
            MediaModerationStatus.UNMODERATED,
            new Date(),
            "Jim Watkins");
    }

    public static Media media2()
    {
        return new Media(2,
            Media.MediaType.IMAGE,
            "https://c2.staticflickr.com/2/1681/25716235400_c83fd098a2_h.jpg",
            "https://c2.staticflickr.com/2/1681/25716235400_b8f3a7b61f_z.jpg",
            "Cat heads in Soma",
            MediaModerationStatus.UNMODERATED,
            new Date(),
            "Fuzzy Traveler");
    }

    public static Media media3()
    {
        return new Media(3,
            Media.MediaType.IMAGE,
            "https://c2.staticflickr.com/2/1471/25704130430_831014a51e_h.jpg",
            "https://c2.staticflickr.com/2/1471/25704130430_d9530e8f6e_z.jpg",
            "Chinatown",
            MediaModerationStatus.UNMODERATED,
            new Date(),
            "JeniKoleva");
    }

    public static Media media4()
    {
        return new Media(4,
            Media.MediaType.IMAGE,
            "https://c1.staticflickr.com/1/554/19849093339_d4f170e4e5_h.jpg",
            "https://c1.staticflickr.com/1/554/19849093339_8cb166e73d_z.jpg",
            "Golden Gate bridge",
            MediaModerationStatus.UNMODERATED,
            new Date(),
            "spiderbhz");
    }

    public static Media media5()
    {
        return new Media(5,
            Media.MediaType.IMAGE,
            "https://c2.staticflickr.com/2/1653/25951852856_d1748effd4_h.jpg",
            "https://c2.staticflickr.com/2/1653/25951852856_dcc9897156_z.jpg",
            "Golden gate by night",
            MediaModerationStatus.UNMODERATED,
            new Date(),
            "kch_86");
    }

    public static Media media6()
    {
        return new Media(6,
            Media.MediaType.IMAGE,
            "https://c2.staticflickr.com/2/1700/25676539050_5275d6a136_h.jpg",
            "https://c2.staticflickr.com/2/1700/25676539050_00625ecfa4_z.jpg",
            "Twin peaks by night",
            MediaModerationStatus.UNMODERATED,
            new Date(),
            "kch_86");
    }

    public static Media media7()
    {
        return new Media(7,
            Media.MediaType.IMAGE,
            "https://c2.staticflickr.com/2/1583/25316020374_9175d1021c_h.jpg",
            "https://c2.staticflickr.com/2/1583/25316020374_269cc5f59d_z.jpg",
            "Streets",
            MediaModerationStatus.UNMODERATED,
            new Date(),
            "Wilson Lam");
    }

    public static Media media8()
    {
        return new Media(8,
            Media.MediaType.IMAGE,
            "https://c1.staticflickr.com/9/8309/8072557147_d4de6b553d_h.jpg",
            "https://c1.staticflickr.com/9/8309/8072557147_98954b57b5_z.jpg",
            "Fort Winfield Scott",
            MediaModerationStatus.UNMODERATED,
            new Date(),
            "Vieuxfinder");
    }

    public static Media media9()
    {
        return new Media(9,
            Media.MediaType.IMAGE,
            "https://c2.staticflickr.com/2/1600/25304701524_6c94f9ae72_h.jpg",
            "https://c2.staticflickr.com/2/1600/25304701524_345aed8151_z.jpg",
            "Houses in San Francisco",
            MediaModerationStatus.UNMODERATED,
            new Date(),
            "birdsongPics");
    }

    public static Media media10()
    {
        return new Media(10,
            Media.MediaType.IMAGE,
            "https://c2.staticflickr.com/2/1615/25300068344_ce3626e6ba_o.jpg",
            "https://c2.staticflickr.com/2/1615/25300068344_6e8c4e8c16_z.jpg",
            "Calm before the storm",
            MediaModerationStatus.UNMODERATED,
            new Date(),
            "Joe B Yumang");
    }
}
